package org.objectweb.asm.idea.stackmachine

import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.project.Project

typealias CommandsMap = Map<Int, StackElement>

interface StackMachineService {
    companion object {
        fun getInstance(project: Project): StackMachineService = ServiceManager.getService(project, StackMachineService::class.java)
    }

    // проинициализировать
    fun initializeClass(map: CommandsMap)
    val stackMachine: StackMachine

    fun emulateMachineUntil(lineNumber: Int)
    fun emulateOneLine(lineNumber: Int)
}
