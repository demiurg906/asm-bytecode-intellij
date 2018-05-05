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
            ICONST_M1, ICONST_0, ICONST_1, ICONST_2, ICONST_3, ICONST_4, ICONST_5,
            FCONST_0, FCONST_1, FCONST_2,
            LCONST_0, LCONST_1,
            DCONST_0, DCONST_1,
            IADD, FADD, LADD,
            DADD, ISUB, FSUB, LSUB, DSUB,
            IMUL, FMUL, LMUL, DMUL, IDIV,
            FDIV, LDIV, DDIV, IREM, LREM,
            FREM, DREM -> lineNumbers.add(super.text.size - 1)
        }
    }

    override fun visitVarInsn(opcode: Int, `var`: Int) {
        super.visitVarInsn(opcode, `var`)

        when (opcode) {
            ILOAD, ISTORE, FLOAD, FSTORE, LSTORE, LLOAD, DLOAD, DSTORE -> lineNumbers.add(super.text.size - 1)
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

    override fun visitLdcInsn(operand: Any?) {
        super.visitLdcInsn(operand)
        when (operand) {
            is Double, is Float, is Long -> lineNumbers.add(super.text.size - 1)
        }
    }

    override fun visitLabel(label: Label) {
        super.visitLabel(label)
        labelToLineNumber[label] = super.text.size
    }
}
