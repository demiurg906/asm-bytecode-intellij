package org.objectweb.asm.idea.stackmachine

data class LocalVariable(val index: Int, val name: String, var value: Int)

class LocalVariableTable(val variables: List<LocalVariable>) {
    fun findVariableByName(name: String): LocalVariable? = variables.find { it.name == name }
    fun findVariableById(id: Int): LocalVariable? = variables.find { it.index == id }
}
