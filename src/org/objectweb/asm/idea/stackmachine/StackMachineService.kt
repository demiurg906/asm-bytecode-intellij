package org.objectweb.asm.idea.stackmachine

import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.project.Project
import org.objectweb.asm.idea.insns.Insn
import org.objectweb.asm.idea.ui.StackViewer

typealias CommandsMap = Map<Int, Insn>

interface StackMachineService {
    companion object {
        fun getInstance(project: Project): StackMachineService = ServiceManager.getService(project, StackMachineService::class.java)
    }

    // проинициализировать
    fun initializeClass(map: CommandsMap)
    val stackMachine: StackMachine
    val stackViewer: StackViewer

    fun emulateMachineUntil(lineNumber: Int)
    fun emulateOneLine(lineNumber: Int)

    fun registerStackViewer(stackViewer: StackViewer)
}
