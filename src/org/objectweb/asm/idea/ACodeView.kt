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
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.diff.DiffContent
import com.intellij.openapi.diff.DiffManager
import com.intellij.openapi.diff.DiffRequest
import com.intellij.openapi.diff.SimpleContent
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.fileTypes.FileTypeManager
import com.intellij.openapi.keymap.KeymapManager
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.openapi.util.IconLoader
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.psi.PsiFileFactory
import com.intellij.ui.PopupHandler
import org.objectweb.asm.idea.actions.EmulateLineAction
import org.objectweb.asm.idea.actions.StartNewStackAction
import org.objectweb.asm.idea.config.ASMPluginComponent
import org.objectweb.asm.idea.stackmachine.StackMachineService
import org.objectweb.asm.idea.ui.StackViewer
import java.awt.BorderLayout
import javax.swing.BoxLayout
import javax.swing.JPanel

/**
 * Base class for editors which displays bytecode or ASMified code.
 */
open class ACodeView @JvmOverloads constructor(
        protected val toolWindowManager: ToolWindowManager,
        protected val keymapManager: KeymapManager,
        protected val project: Project,
        private val extension: String = "java") : SimpleToolWindowPanel(true, true), Disposable {

    protected var _editor: Editor?
    private val editor: Editor
        get() = _editor!!

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
        mainPanel.layout = BoxLayout(mainPanel, BoxLayout.Y_AXIS)

        val editorComponent = editor.component
        mainPanel.add(editorComponent)

        val stackViewer = StackViewer()
        val service = StackMachineService.getInstance(project)
        service.registerStackViewer(stackViewer)
        service.registerBytecodeEditor(editor)
        mainPanel.add(stackViewer.component)

        add(mainPanel)

        val diffAction = createShowDiffAction()
        val group = DefaultActionGroup()
        group.add(diffAction)
        group.add(ShowSettingsAction())
        group.add(StartNewStackAction())
        group.add(EmulateLineAction())

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
            previousCode = "" // reset previous code
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

    private fun createShowDiffAction(): AnAction {
        return ShowDiffAction()
    }

    private inner class ShowSettingsAction : AnAction("Settings", "Show settings for ASM plugin", IconLoader.getIcon("/general/projectSettings.png")) {

        override fun displayTextInToolbar(): Boolean {
            return true
        }

        override fun actionPerformed(e: AnActionEvent) {
            ShowSettingsUtil.getInstance().showSettingsDialog(project, project.getComponent(ASMPluginComponent::class.java))
        }
    }

    private inner class ShowDiffAction : AnAction("Show differences", "Shows differences from the previous version of bytecode for this file", IconLoader.getIcon("/actions/diffWithCurrent.png")) {

        override fun update(e: AnActionEvent?) {
            e!!.presentation.isEnabled = ("" != previousCode) && (previousFile != null)
        }

        override fun displayTextInToolbar(): Boolean {
            return true
        }

        override fun actionPerformed(e: AnActionEvent) {
            DiffManager.getInstance().diffTool.show(object : DiffRequest(project) {
                override fun getContents(): Array<DiffContent> {
                    // there must be a simpler way to obtain the file type
                    val psiFile = PsiFileFactory.getInstance(project).createFileFromText("asm.$extension", "")
                    val currentContent = if (previousFile == null) SimpleContent("") else SimpleContent(document.text, psiFile.fileType)
                    val oldContent = SimpleContent(previousCode ?: "", psiFile.fileType)
                    return arrayOf(oldContent, currentContent)
                }

                override fun getContentTitles(): Array<String> {
                    return DIFF_TITLES
                }

                override fun getWindowTitle(): String {
                    return DIFF_WINDOW_TITLE
                }
            })
        }
    }

    companion object {
        private val DIFF_WINDOW_TITLE = "Show differences from previous class contents"
        private val DIFF_TITLES = arrayOf("Previous version", "Current version")
    }
}
