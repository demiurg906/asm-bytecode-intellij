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

    // TODO remove this
    fun initializeClass(map: CommandsMap)
    fun initializeClass(params: StackParams) {
        initializeClass(params.commandsMap)
    }
    fun resetStack()

    fun emulateToCursor()
    fun emulateOneLine()

    fun registerStackViewer(stackViewer: StackViewer)
    fun registerBytecodeEditor(editor: Editor)
}
