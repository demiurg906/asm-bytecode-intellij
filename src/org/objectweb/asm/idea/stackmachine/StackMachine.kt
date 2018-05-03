package org.objectweb.asm.idea.stackmachine

import org.objectweb.asm.idea.insns.Insn

sealed class StackElement
data class StackValue<T>(val value: T) : StackElement()
data class StackVariable(val name: String) : StackElement()

data class LocalVariable(val name: String, var value: Int)


interface StackMachine {
    companion object {
        fun getInstance(): StackMachine = TODO("Ромчик, дерзай")
    }

    val stack: List<StackElement>
    val variables: Map<Int, LocalVariable>
    fun execute(insn: Insn)
}
