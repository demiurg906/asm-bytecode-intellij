package org.objectweb.asm.idea.stackmachine

data class LocalVariable(val index: Int, val name: String, var value: Int)

/**
 * Contains list of variables, visible from method.
 *
 * It is supposed that list of variables cannot be changed.
 */
class LocalVariableTable(val variables: List<LocalVariable>) {

    fun findVariableByName(name: String): LocalVariable? = variables.find { it.name == name }

    fun findVariableById(id: Int): LocalVariable? = variables.find { it.index == id }

    fun setVariableByName(name: String, value: Int) {
        val variable = findVariableByName(name) ?: throw IllegalArgumentException("No variable with name $name.")
        variable.value = value
    }

    fun setVariableById(id: Int, value: Int) {
        val variable = findVariableById(id) ?: throw IllegalArgumentException("No variable with id $id.")
        variable.value = value
    }

}
