package org.objectweb.asm.idea.visitors

import reloc.org.objectweb.asm.ClassVisitor
import reloc.org.objectweb.asm.MethodVisitor
import reloc.org.objectweb.asm.Opcodes
import reloc.org.objectweb.asm.util.TraceMethodVisitor

import java.util.*

class ClassInsnCollector : ClassVisitor(Opcodes.ASM5), Opcodes {
    var methodVisitors: MutableList<MethodInsnCollector> = LinkedList()
    var printers: MutableList<MethodTextifier> = LinkedList()


    override fun visitMethod(access: Int, name: String?, desc: String?,
                             signature: String?, exceptions: Array<out String>?): MethodVisitor {
        val mv = MethodInsnCollector(access, name, desc, signature, exceptions)
        val mp = MethodTextifier()
        methodVisitors.add(mv)
        printers.add(mp)
        return TraceMethodVisitor(mv, mp)
    }
}