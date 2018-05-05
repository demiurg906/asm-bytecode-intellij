package org.objectweb.asm.idea.visitors

import org.objectweb.asm.idea.insns.*
import reloc.org.objectweb.asm.Label
import reloc.org.objectweb.asm.Opcodes
import reloc.org.objectweb.asm.Opcodes.*
import reloc.org.objectweb.asm.tree.MethodNode

class MethodInsnCollector(access: Int, name: String?,
                          desc: String?, signature: String?,
                          exceptions: Array<out String>?) : MethodNode(ASM5, access, name, desc, signature, exceptions) {

    val collectedInstructions: MutableList<Insn> = mutableListOf()


    override fun visitInsn(opcode: Int) {
        when (opcode) {
        // constants
            ICONST_M1 -> {
                collectedInstructions.add(IntConst(Opcodes.ICONST_M1, -1))
            }

            ICONST_0 -> {
                collectedInstructions.add(IntConst(Opcodes.ICONST_0, 0))
            }

            ICONST_1 -> {
                collectedInstructions.add(IntConst(ICONST_1, 1))
            }

            ICONST_2 -> {
                collectedInstructions.add(IntConst(ICONST_2, 2))
            }

            ICONST_3 -> {
                collectedInstructions.add(IntConst(ICONST_3, 3))
            }

            ICONST_4 -> {
                collectedInstructions.add(IntConst(ICONST_4, 4))
            }

            ICONST_5 -> {
                collectedInstructions.add(IntConst(ICONST_5, 5))
            }

            DCONST_0 -> {
                collectedInstructions.add(DoubleConst(DCONST_0, 0.0))
            }

            DCONST_1 -> {
                collectedInstructions.add(DoubleConst(DCONST_1, 1.0))
            }

            LCONST_0 -> {
                collectedInstructions.add(LongConst(LCONST_0, 0))
            }

            LCONST_1 -> {
                collectedInstructions.add(LongConst(LCONST_1, 0))
            }

            FCONST_0 -> {
                collectedInstructions.add(FloatConst(FCONST_0, 0f))
            }

            FCONST_1 -> {
                collectedInstructions.add(FloatConst(FCONST_1, 1f))
            }

            FCONST_2 -> {
                collectedInstructions.add(FloatConst(FCONST_2, 2f))
            }

        // binary opcodes
            IADD, FADD,
            LADD, DADD -> {
                collectedInstructions.add(BinaryOperation(opcode, OperatorType.ADD, primitiveTypeFromOpcode(opcode)))
            }
            ISUB, FSUB,
            LSUB, DSUB -> {
                collectedInstructions.add(BinaryOperation(opcode, OperatorType.SUBTRACT, primitiveTypeFromOpcode(opcode)))
            }

            IMUL, FMUL,
            LMUL, DMUL -> {
                collectedInstructions.add(BinaryOperation(opcode, OperatorType.MULTIPLY, primitiveTypeFromOpcode(opcode)))
            }

            IDIV, FDIV,
            LDIV, DDIV -> {
                collectedInstructions.add(BinaryOperation(opcode, OperatorType.DIVIDE, primitiveTypeFromOpcode(opcode)))
            }

            IREM, LREM,
            FREM, DREM -> {
                collectedInstructions.add(BinaryOperation(opcode, OperatorType.REMAINDER, primitiveTypeFromOpcode(opcode)))
            }

            LCMP, FCMPL, FCMPG,
            DCMPL, DCMPG -> {
                collectedInstructions.add(CompareOperation(opcode, comparatorTypeFromOpcode(opcode), primitiveTypeFromOpcode(opcode)))
            }

        }
        super.visitInsn(opcode)
    }

    override fun visitVarInsn(opcode: Int, localIdx: Int) {
        when (opcode) {
            ILOAD -> {
                collectedInstructions.add(LocalLoad(opcode, localIdx, primitiveTypeFromOpcode(opcode)))
            }

            ISTORE -> {
                collectedInstructions.add(LocalStore(opcode, localIdx, primitiveTypeFromOpcode(opcode)))
            }

        }
        super.visitVarInsn(opcode, localIdx)
    }

    override fun visitIntInsn(opcode: Int, operand: Int) {
        when (opcode) {
            BIPUSH, SIPUSH -> {
                collectedInstructions.add(IntConst(opcode, operand))
            }
        }

        super.visitIntInsn(opcode, operand)
    }

    override fun visitJumpInsn(opcode: Int, label: Label) {
        when (opcode) {
            IF_ICMPEQ, IF_ICMPGE, IF_ICMPGT,
            IF_ICMPLE, IF_ICMPLT, IF_ICMPNE -> {
                collectedInstructions.add(IntCompareJump(opcode, comparatorTypeFromOpcode(opcode), label))
            }

            IFNONNULL -> {
                collectedInstructions.add(NullCompareJump(opcode, ComparatorType.NOT_EQUAL, label))
            }

            IFNULL -> {
                collectedInstructions.add(NullCompareJump(opcode, ComparatorType.EQUAL, label))
            }

            IFEQ, IFGE, IFGT,
            IFLE, IFLT, IFNE -> {
                collectedInstructions.add(ZeroCompareJump(opcode, comparatorTypeFromOpcode(opcode), label))
            }

        }

        super.visitJumpInsn(opcode, label)
    }

    private fun primitiveTypeFromOpcode(opcode: Int): PrimitiveType {
        return when (opcode) {
            IADD, ISUB, IMUL,
            IDIV, IREM, ILOAD,
            ISTORE -> PrimitiveType.INT

            FADD, FSUB, FMUL,
            FDIV, FREM, FLOAD,
            FSTORE -> PrimitiveType.FLOAT

            LADD, LSUB, LMUL,
            LDIV, LREM, LLOAD,
            LSTORE -> PrimitiveType.LONG

            DADD, DSUB, DMUL,
            DDIV, DREM, DLOAD,
            DSTORE -> PrimitiveType.DOUBLE
            else -> throw IllegalArgumentException("invalid opcode")
        }

    }

    private fun comparatorTypeFromOpcode(opcode: Int): ComparatorType {
        return when (opcode) {
            IF_ICMPEQ, IFEQ -> ComparatorType.EQUAL
            IF_ICMPGE, IFGE -> ComparatorType.GREATER_EQUAL
            IF_ICMPGT, IFGT, FCMPG, DCMPG -> ComparatorType.GREATER
            IF_ICMPLE, IFLE -> ComparatorType.LESS_EQUAL
            IF_ICMPLT, IFLT, FCMPL,
            LCMP, DCMPL -> ComparatorType.LESS
            IF_ICMPNE, IFNE -> ComparatorType.NOT_EQUAL
            else -> throw IllegalArgumentException("invalid opcode")
        }

    }
}