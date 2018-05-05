package org.objectweb.asm.idea.visitors

class MethodPsiInfo(
        val functionName: String,
        val returnType: String,
        val parameterTypes: List<String>
) {

    val descriptor: String
        get() {
            val argDescriptor = parameterTypes.map(::convertToDescriptor)
                    .joinToString("")
            val returnDescriptor = convertToDescriptor(returnType)
            return "($argDescriptor)$returnDescriptor"
        }


    private fun convertToDescriptor(s: String): String {
        return when (s.toLowerCase()) {
            "byte" -> "B"
            "char" -> "C"
            "double" -> "D"
            "float" -> "F"
            "int" -> "I"
            "long" -> "J"
            "short" -> "S"
            "boolean" -> "Z"
            else -> "L${s.replace(".", "/")};"
        }
    }
}

