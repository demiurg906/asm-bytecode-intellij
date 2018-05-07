package org.objectweb.asm.idea.stackmachine

interface LocalVariable {
    val index: Int
    val name: String
    val value: Number?
    fun reset()
}

data class IntVariable(override val index: Int, override val name: String, override var value: Int? = null): LocalVariable {
    override fun reset() { value = null }
}
data class LongVariable(override val index: Int, override val name: String, override var value: Long? = null): LocalVariable {
    override fun reset() { value = null }
}
data class FloatVariable(override val index: Int, override val name: String, override var value: Float? = null): LocalVariable {
    override fun reset() { value = null }
}
data class DoubleVariable(override val index: Int, override val name: String, override var value: Double? = null): LocalVariable {
    override fun reset() { value = null }
}

/**
 * Contains list of variables, visible from method.
 *
 * It is supposed that list of variables cannot be changed.
 */
class LocalVariableTable(val variables: Collection<LocalVariable>) {

    fun findVariableByName(name: String): LocalVariable? = variables.find { it.name == name }

    fun findVariableById(id: Int): LocalVariable? = variables.find { it.index == id }

    fun setVariableByName(name: String, value: Int) {
        val variable = findVariableByName(name) as? IntVariable ?: throw IllegalArgumentException("No variable with name $name.")
        variable.value = value
    }

    fun setVariableById(id: Int, value: Int) {
        val variable = findVariableById(id) as? IntVariable ?: throw IllegalArgumentException("No variable with id $id.")
        variable.value = value
    }

    fun setVariableById(id: Int, value: Long) {
        val variable = findVariableById(id) as? LongVariable ?: throw IllegalArgumentException("No variable with id $id.")
        variable.value = value
    }

    fun setVariableById(id: Int, value: Double) {
        val variable = findVariableById(id) as? DoubleVariable ?: throw IllegalArgumentException("No variable with id $id.")
        variable.value = value
    }

    fun setVariableById(id: Int, value: Float) {
        val variable = findVariableById(id) as? FloatVariable ?: throw IllegalArgumentException("No variable with id $id.")
        variable.value = value
    }


    fun resetState() {
        variables.forEach { it.reset() }
    }

    companion object {
        val emptyTable = LocalVariableTable(emptyList())
    }

}
