package org.objectweb.asm.idea.stackmachine

import org.objectweb.asm.idea.insns.Insn

sealed class StackElement
data class StackValue<T>(val value: T) : StackElement()
data class StackVariable(val name: String) : StackElement()

interface StackMachine {
    val stack: List<StackElement>
    fun execute(insn: Insn)
}
