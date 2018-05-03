package org.objectweb.asm.idea.stackmachine

import org.objectweb.asm.idea.insns.Insn

class DummyStackMachine : StackMachine {
    override val stack: List<StackElement>
        get() = listOf()
    override val variables: Map<Int, LocalVariable>
        get() = mapOf()

    override fun execute(insn: Insn) {
        println("$insn executed")
    }
}