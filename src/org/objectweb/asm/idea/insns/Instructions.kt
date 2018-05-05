package org.objectweb.asm.idea.insns

import reloc.org.objectweb.asm.Label

sealed class Instruction

enum class OperatorType {
    ADD, SUBTRACT, REMAINDER,
    MULTIPLY, DIVIDE;
}

enum class PrimitiveType {
    DOUBLE, INT, FLOAT, LONG;
}

data class BinaryOperation(val op: OperatorType, val type: PrimitiveType) : Instruction()

data class LocalLoad(val index: Int, val type: PrimitiveType) : Instruction()
data class LocalStore(val index: Int, val type: PrimitiveType) : Instruction()

data class IntConst(val operand: Int) : Instruction()
data class DoubleConst(val operand: Double) : Instruction()
data class LongConst(val operand: Long) : Instruction()
data class FloatConst(val operand: Float) : Instruction()


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
data class CompareOperation(val comparatorType: ComparatorType,
                            val type: PrimitiveType) : Instruction()


// !! has reference to another command
// if_icmpeq
// if_icmpge
// if_icmpgt
// if_icmple
// if_icmplt
// if_icmpne
data class IntCompareJump(val comparatorType: ComparatorType,
                          val target: Label) : Instruction()

// !! has reference to another command
// ifeq
// ifge
// ifgt
// ifle
// iflt
data class ZeroCompareJump(val comparatorType: ComparatorType,
                           val target: Label) : Instruction()

// !! has reference to another command
// ifnonnull
// ifnull
/**
 * [comparatorType] has value EQUAL if equals to null operation,
 * NOT_EQUAL otherwise
 */
data class NullCompareJump(val comparatorType: ComparatorType,
                           val target: Label) : Instruction()

data class Goto(val target: Label): Instruction()


// TODO if_acmpne
