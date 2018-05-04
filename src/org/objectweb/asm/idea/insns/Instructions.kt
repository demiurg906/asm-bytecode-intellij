package org.objectweb.asm.idea.insns

sealed class Insn(open val opcode: Int)

enum class OperatorType {
    ADD, SUBTRACT, REMAINDER,
    MULTIPLY, DIVIDE;
}

data class BinaryOperation(override val opcode: Int, val op: OperatorType) : Insn(opcode)

data class IntConst(override val opcode: Int, val operand: Int) : Insn(opcode)

data class LocalLoad(override val opcode: Int, val index: Int) : Insn(opcode)

data class LocalStore(override val opcode: Int, val index: Int) : Insn(opcode)