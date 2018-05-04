package org.objectweb.asm.idea.visitors

import org.objectweb.asm.idea.insns.*
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



        // binary opcodes
            IADD, FADD,
            LADD, DADD -> {
                collectedInstructions.add(BinaryOperation(opcode, OperatorType.ADD))
            }
            ISUB, FSUB,
            LSUB, DSUB -> {
                collectedInstructions.add(BinaryOperation(opcode, OperatorType.SUBTRACT))
            }

            IMUL, FMUL,
            LMUL, DMUL -> {
                collectedInstructions.add(BinaryOperation(opcode, OperatorType.MULTIPLY))
            }

            IDIV, FDIV,
            LDIV, DDIV -> {
                collectedInstructions.add(BinaryOperation(opcode, OperatorType.DIVIDE))
            }

            IREM, LREM,
            FREM, DREM -> {
                collectedInstructions.add(BinaryOperation(opcode, OperatorType.REMAINDER))
            }

        }
    }

    override fun visitVarInsn(opcode: Int, localIdx: Int) {
        when (opcode) {
            ILOAD -> {
                collectedInstructions.add(LocalLoad(opcode, localIdx))
            }

            ISTORE -> {
                collectedInstructions.add(LocalStore(opcode, localIdx))
            }

        }
    }
}