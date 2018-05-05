package org.objectweb.asm.idea.stackmachine

import org.objectweb.asm.idea.insns.*

class StackMachineImpl : StackMachine {
    private val _stack = mutableListOf<StackElement>()
    private val _variables = mutableMapOf<Int, LocalVariable>()

    override val stack: List<StackElement>
        get() = _stack.toList()

    override val variables: Map<Int, LocalVariable>
        get() = _variables.toMap()

    override val localVariables
        get() = LocalVariableTable(variables.values.toList())

    override fun execute(insn: Insn): StackOperationResult {
        return when (insn) {
            is IntConst -> pushInt(insn.operand)
            is LocalLoad -> pushVariable(insn.index)
            is LocalStore -> storeVariable(insn.index)
            is BinaryOperation -> executeBinaryOperation(insn.op)
            else -> TODO("$insn is not handled yet.")
        }
    }

    private fun executeBinaryOperation(op: OperatorType): StackOperationResult {
        val right = (_stack.pop()?.value) ?: throw IllegalArgumentException("No first argument for operation $op.")
        val left = (_stack.pop()?.value) ?: throw IllegalArgumentException("No second argument for operation $op.")

        val result = when (op) {
            OperatorType.ADD -> left + right
            OperatorType.SUBTRACT -> left - right
            OperatorType.MULTIPLY -> left * right
            OperatorType.DIVIDE -> left / right
            OperatorType.REMAINDER -> left % right
        }

        pushInt(result)

        return StackOperationResult(removed = 2, addedCells = _stack.takeLast(1))
    }

    private fun pushInt(i: Int): StackOperationResult {
        _stack.add(StackElement(i))
        return StackOperationResult(removed = 0, addedCells = _stack.takeLast(1))
    }

    private fun storeVariable(index: Int): StackOperationResult {
        val value = _stack.pop()?.value
                ?: throw IllegalArgumentException("No elements on stack to store in variable $index.")
        _variables[index] = LocalVariable(index, "var_$index", value)

        return StackOperationResult(removed = 1, addedCells = emptyList())
    }

    private fun pushVariable(index: Int): StackOperationResult {
        val variable = _variables[index]
                ?: throw IllegalArgumentException("No name for variable with index $index is found!")

        return pushInt(variable.value)
    }
}

private fun <T> MutableList<T>.pop(): T? {
    if (this.isNotEmpty()) {
        return this.removeAt(this.size - 1)
    }

    return null
}