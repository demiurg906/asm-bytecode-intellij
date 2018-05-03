package org.objectweb.asm.idea.stackmachine

import org.objectweb.asm.idea.ui.StackViewer

class StackMachineServiceImpl : StackMachineService {
    override fun initializeClass(map: CommandsMap) {
        _stackMachine = StackMachine.getInstance()
        commandsMap = map
    }

    private var _stackMachine: StackMachine = StackMachine.getInstance()
    private var commandsMap: CommandsMap = mapOf()
    override val stackViewer: StackViewer
        get() = _stackViewer!!
    private var _stackViewer: StackViewer? = null


    override val stackMachine: StackMachine
        get() = _stackMachine

    override fun emulateMachineUntil(lineNumber: Int) {
        for (line in 0 until lineNumber) {
            val command = commandsMap[line] ?: throw StackEvaluationException("no command under line $line")
            _stackMachine.execute(command)
        }
    }

    override fun emulateOneLine(lineNumber: Int) {
        val command = commandsMap[lineNumber] ?: throw StackEvaluationException("no command under line $lineNumber")
        _stackMachine.execute(command)
    }

    override fun registerStackViewer(stackViewer: StackViewer) {
        this._stackViewer = stackViewer
    }
}