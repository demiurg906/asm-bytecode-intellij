package org.objectweb.asm.idea.visitors

import reloc.org.objectweb.asm.ClassVisitor
import reloc.org.objectweb.asm.MethodVisitor
import reloc.org.objectweb.asm.Opcodes
import reloc.org.objectweb.asm.util.TraceMethodVisitor

class ClassInsnCollector : ClassVisitor(Opcodes.ASM5), Opcodes {
    val methodVisitors: MutableList<MethodInsnCollector> = mutableListOf()
    val printers: MutableList<MethodTextifier> = mutableListOf()

    override fun visitMethod(access: Int, name: String?, desc: String?,
                             signature: String?, exceptions: Array<out String>?): MethodVisitor {
        val mv = MethodInsnCollector(access, name, desc, signature, exceptions)
        val mp = MethodTextifier()
        methodVisitors.add(mv)
        printers.add(mp)
        return TraceMethodVisitor(mv, mp)
    }
}