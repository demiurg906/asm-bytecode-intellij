/*
 *
 *  Copyright 2011 Cédric Champeau
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * /
 */

package org.objectweb.asm.idea

/**
 * Created by IntelliJ IDEA.
 * User: cedric
 * Date: 07/01/11
 * Time: 22:18
 */

import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.fileTypes.FileTypeManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.PopupHandler
import org.objectweb.asm.idea.actions.EmulateLineAction
import org.objectweb.asm.idea.actions.EmulateToCursorAction
import org.objectweb.asm.idea.actions.ResetStackAction
import org.objectweb.asm.idea.stackmachine.StackMachineService
import org.objectweb.asm.idea.ui.StackViewer
import java.awt.BorderLayout
import java.awt.GridLayout
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import javax.swing.JPanel

/**
 * Base class for editors which displays bytecode or ASMified code.
 */
class BytecodeOutline @JvmOverloads constructor(
        private val project: Project,
        extension: String = "java") : SimpleToolWindowPanel(true, true), Disposable {

    companion object {
        fun getInstance(project: Project): BytecodeOutline = ServiceManager.getService(project, BytecodeOutline::class.java)
    }

    private val editor: Editor
        get() = _editor!!
    private var _editor: Editor?

    protected val document: Document

    // used for diff view
    private var previousCode: String? = null
    private var previousFile: VirtualFile? = null

    init {
        val editorFactory = EditorFactory.getInstance()
        document = editorFactory.createDocument("")
        _editor = editorFactory.createEditor(document, project, FileTypeManager.getInstance().getFileTypeByExtension(extension), true)
        setupUI()
    }

    private fun setupUI() {
        val mainPanel = JPanel()
        mainPanel.layout = GridLayout(2, 1)

        val editorComponent = editor.component
        mainPanel.add(editorComponent)

        editor.contentComponent.addKeyListener(object : KeyListener {
            private val service = StackMachineService.getInstance(project)

            override fun keyTyped(event: KeyEvent) {}

            override fun keyPressed(event: KeyEvent) {
                when (event.keyCode) {
                    KeyEvent.VK_F7 -> service.emulateToCursor()
                    KeyEvent.VK_F8 -> service.emulateOneLine()
                }
            }

            override fun keyReleased(event: KeyEvent) {}
        })

        val stackViewer = StackViewer()
        val service = StackMachineService.getInstance(project)
        service.registerStackViewer(stackViewer)
        service.registerBytecodeEditor(editor)
        mainPanel.add(stackViewer.component)

        add(mainPanel)

        val group = DefaultActionGroup()
        group.add(EmulateToCursorAction())
        group.add(EmulateLineAction())
        group.add(ResetStackAction())

        val actionManager = ActionManager.getInstance()
        val actionToolBar = actionManager.createActionToolbar("ASM", group, true)
        val buttonsPanel = JPanel(BorderLayout())
        buttonsPanel.add(actionToolBar.component, BorderLayout.CENTER)
        PopupHandler.installPopupHandler(editor.contentComponent, group, "ASM", actionManager)
        setToolbar(buttonsPanel)
    }

    fun setCode(file: VirtualFile?, code: String) {
        val text = document.text
        if (previousFile == null || file == null || previousFile!!.path == file.path && Constants.NO_CLASS_FOUND != text) {
            if (file != null) previousCode = text
        } else if (previousFile!!.path != file.path) {
            // reset previous code
            previousCode = ""
        }
        document.setText(code)
        if (file != null) previousFile = file
    }


    override fun dispose() {
        if (_editor == null) return

        val editorFactory = EditorFactory.getInstance()
        editorFactory.releaseEditor(editor)
        _editor = null
    }
}
