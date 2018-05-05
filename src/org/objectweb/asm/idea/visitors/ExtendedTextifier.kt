package org.objectweb.asm.idea.visitors

import reloc.org.objectweb.asm.Label
import reloc.org.objectweb.asm.Opcodes.*
import reloc.org.objectweb.asm.util.Textifier
import java.io.PrintWriter
import java.io.StringWriter

class MethodTextifier : Textifier(ASM5) {
    val lineNumbers: MutableList<Int> = mutableListOf()
    val labelToLineNumber: MutableMap<Label, Int> = mutableMapOf()

    val collectedText
        get(): String {
            val stringWriter = StringWriter()

            PrintWriter(stringWriter).use { print(it) }

            return stringWriter.toString()
        }

    override fun visitInsn(opcode: Int) {
        super.visitInsn(opcode)

        when (opcode) {
            ICONST_M1, ICONST_0, ICONST_1,
            ICONST_2, ICONST_3, ICONST_4,
            ICONST_5, IADD, FADD, LADD,
            DADD, ISUB, FSUB, LSUB, DSUB,
            IMUL, FMUL, LMUL, DMUL, IDIV,
            FDIV, LDIV, DDIV, IREM, LREM,
            FREM, DREM -> lineNumbers.add(super.text.size - 1)
        }
    }

    override fun visitVarInsn(opcode: Int, `var`: Int) {
        super.visitVarInsn(opcode, `var`)

        when (opcode) {
            ILOAD, ISTORE -> lineNumbers.add(super.text.size - 1)
        }
    }

    override fun visitIntInsn(opcode: Int, operand: Int) {
        super.visitIntInsn(opcode, operand)

        when (opcode) {
            BIPUSH -> {
                lineNumbers.add(super.text.size - 1)
            }
        }
    }

    override fun visitJumpInsn(opcode: Int, label: Label?) {
        when(opcode) {
            IF_ICMPEQ, IF_ICMPGE, IF_ICMPGT,
            IF_ICMPLE, IF_ICMPLT, IF_ICMPNE -> lineNumbers.add(super.text.size - 1)
            /*IFNONNULL, IFNULL, IFEQ, IFGE, IFGT,
            IFLE, IFLT, IFNE, GOTO*/
        }
    }

    override fun visitLabel(label: Label) {
        super.visitLabel(label)
        labelToLineNumber[label] = super.text.size
    }
}
