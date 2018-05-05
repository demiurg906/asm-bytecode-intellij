package org.objectweb.asm.idea.insns

sealed class Insn(open val opcode: Int)

enum class OperatorType {
    ADD, SUBTRACT, REMAINDER,
    MULTIPLY, DIVIDE;
}

enum class PrimitiveType {
    DOUBLE, INT, FLOAT, LONG;
}

data class BinaryOperation(override val opcode: Int, val op: OperatorType, val type: PrimitiveType) : Insn(opcode)

data class LocalLoad(override val opcode: Int, val index: Int, val type: PrimitiveType) : Insn(opcode)
data class LocalStore(override val opcode: Int, val index: Int, val type: PrimitiveType) : Insn(opcode)

data class IntConst(override val opcode: Int, val operand: Int) : Insn(opcode)
data class DoubleConst(override val opcode: Int, val operand: Double) : Insn(opcode)
data class LongConst(override val opcode: Int, val operand: Long) : Insn(opcode)
data class FloatConst(override val opcode: Int, val operand: Float) : Insn(opcode)