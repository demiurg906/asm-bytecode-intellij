package org.objectweb.asm.idea.visitors

import reloc.org.objectweb.asm.Opcodes.ASM5
import reloc.org.objectweb.asm.util.Textifier

class MethodTextifier : Textifier(ASM5) {
    val lineNumbers: MutableList<Int> = mutableListOf()
    override fun visitVarInsn(opcode: Int, `var`: Int) {
        super.visitVarInsn(opcode, `var`)
        lineNumbers.add(super.text.size - 1)
    }

    override fun visitInsn(opcode: Int) {
        super.visitInsn(opcode)
        lineNumbers.add(super.text.size - 1)
    }
}
