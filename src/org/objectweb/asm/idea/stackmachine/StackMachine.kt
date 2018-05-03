package org.objectweb.asm.idea.stackmachine

import org.objectweb.asm.idea.insns.Insn

//sealed class StackElement
data class StackElement(val value: Int)
//data class StackVariable(val name: String) : StackElement()

data class LocalVariable(val name: String, var value: Int)

interface StackMachine {
    companion object {
        private val machine = StackMachineImpl()
        fun getInstance(): StackMachine = machine
    }

    val stack: List<StackElement>
    val variables: Map<Int, LocalVariable>
    fun execute(insn: Insn)
}
