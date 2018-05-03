package org.objectweb.asm.idea.insns

interface Insn {
    val opcode: Int
}

enum class OperatorType

data class BinaryOperation(override val opcode: Int, val op: OperatorType) : Insn

data class IntConst(override val opcode: Int, val operand: Int) : Insn

data class LocalLoad(override val opcode: Int, val index: Int) : Insn


