package org.objectweb.asm.idea.visitors

data class MethodPsiInfo(
        val functionName: String,
        val returnType: String,
        val parameterTypes: List<String>
)