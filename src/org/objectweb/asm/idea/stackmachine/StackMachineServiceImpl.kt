package org.objectweb.asm.idea.stackmachine

class StackMachineServiceImpl : StackMachineService {
    override fun initializeClass(map: Map<Int, StackElement>) {
        TODO("not implemented")
    }

    private var _stackMachine: StackMachine = TODO()

    override val stackMachine: StackMachine
        get() = _stackMachine

    override fun emulateMachineUntil(lineNumber: Int) {
        TODO("not implemented")
    }

    override fun emulateOneLine(lineNumber: Int) {
        TODO("not implemented")
    }
}