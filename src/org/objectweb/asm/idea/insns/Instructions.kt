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


enum class ComparatorType {
    LESS, LESS_EQUAL, GREATER,
    GREATER_EQUAL, EQUAL, NOT_EQUAL
}

/**
 * ComareOperation holds commands:
 * LCMP — push 0 if the two longs are the same, 1 if value1 is greater than value2, -1 otherwise
 * FCMPL — compare two floats
 * FCMPG — compare two floats
 * DCMPL — compare two doubles
 * DCMPG — compare two doubles
 */
data class CompareOperation(override val opcode: Int, val comparatorType: ComparatorType, val type: PrimitiveType) : Insn(opcode)


// !! has reference to another command
// if_icmpeq
// if_icmpge
// if_icmpgt
// if_icmple
// if_icmplt
// if_icmpne
data class IntCompareJump(override val opcode: Int, val comparatorType: ComparatorType) : Insn(opcode)

// !! has reference to another command
// ifeq
// ifge
// ifgt
// ifle
// iflt
data class ZeroCompareJump(override val opcode: Int, val comparatorType: ComparatorType) : Insn(opcode)

// !! has reference to another command
// ifnonnull
// ifnull
/**
 * [comparatorType] has value EQUAL if equals to null operation,
 * NOT_EQUAL otherwise
 */
data class NullCompareJump(override val opcode: Int, val comparatorType: ComparatorType) : Insn(opcode)



data class Goto(override val opcode: Int): Insn(opcode)
// TODO if_acmpne