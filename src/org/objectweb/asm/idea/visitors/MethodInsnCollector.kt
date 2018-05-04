package org.objectweb.asm.idea.visitors

import org.objectweb.asm.idea.insns.*
import org.objectweb.asm.idea.stackmachine.LocalVariable
import reloc.org.objectweb.asm.tree.LocalVariableNode
import reloc.org.objectweb.asm.Opcodes
import reloc.org.objectweb.asm.Opcodes.*
import reloc.org.objectweb.asm.tree.MethodNode

class MethodInsnCollector(access: Int, name: String?,
                          desc: String?, signature: String?,
                          exceptions: Array<out String>?) : MethodNode(ASM5, access, name, desc, signature, exceptions) {

    val collectedInstructions: MutableList<Insn> = mutableListOf()

    @Suppress("UNCHECKED_CAST")
    val localVariablesTyped
        get() = (localVariables as List<LocalVariableNode>).map { LocalVariable(it.index, it.name, 0) }

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
                collectedInstructions.add(BinaryOperation(opcode, OperatorType.ADD, getType(opcode)))
            }
            ISUB, FSUB,
            LSUB, DSUB -> {
                collectedInstructions.add(BinaryOperation(opcode, OperatorType.SUBTRACT, getType(opcode)))
            }

            IMUL, FMUL,
            LMUL, DMUL -> {
                collectedInstructions.add(BinaryOperation(opcode, OperatorType.MULTIPLY, getType(opcode)))
            }

            IDIV, FDIV,
            LDIV, DDIV -> {
                collectedInstructions.add(BinaryOperation(opcode, OperatorType.DIVIDE, getType(opcode)))
            }

            IREM, LREM,
            FREM, DREM -> {
                collectedInstructions.add(BinaryOperation(opcode, OperatorType.REMAINDER, getType(opcode)))
            }

        }
    }

    override fun visitVarInsn(opcode: Int, localIdx: Int) {
        when (opcode) {
            ILOAD -> {
                collectedInstructions.add(LocalLoad(opcode, localIdx, getType(opcode)))
            }

            ISTORE -> {
                collectedInstructions.add(LocalStore(opcode, localIdx, getType(opcode)))
            }

        }
    }

    override fun visitIntInsn(opcode: Int, operand: Int) {
        when (opcode) {
            BIPUSH, SIPUSH -> {
                collectedInstructions.add(IntConst(opcode, operand))
            }
        }
    }


    private fun getType(opcode: Int): PrimitiveType {
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
}