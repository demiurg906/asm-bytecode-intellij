package org.objectweb.asm.idea.stackmachine

import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import org.objectweb.asm.idea.insns.Insn
import org.objectweb.asm.idea.ui.StackViewer

typealias CommandsMap = Map<Int, Insn>

/**
 * Parameters to initialize stack state.
 */
data class StackParams(val commandsMap: CommandsMap, val localVariables: LocalVariableTable)

interface StackMachineService {
    companion object {
        fun getInstance(project: Project): StackMachineService = ServiceManager.getService(project, StackMachineService::class.java)
    }

    val stackViewer: StackViewer

    fun initializeClass(params: StackParams)
    fun resetStack()

    fun emulateToCursor()
    fun emulateOneLine()

    fun registerBytecodeEditor(editor: Editor)
}
