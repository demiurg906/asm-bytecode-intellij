package org.objectweb.asm.idea.stackmachine

import org.objectweb.asm.idea.insns.*

class StackMachineImpl : StackMachine {
    private val _stack = mutableListOf<StackElement>()
    private val _variables = mutableMapOf<Int, LocalVariable>()

    override val stack: List<StackElement>
        get() = _stack.toList()

    override val variables: Map<Int, LocalVariable>
        get() = _variables.toMap()

    override fun execute(insn: Insn) {
        when (insn) {
            is IntConst -> pushInt(insn.operand)
            is LocalLoad -> pushVariable(insn.index)
            is BinaryOperation -> executeBinaryOperation(insn.op)
        }
    }

    private fun executeBinaryOperation(op: OperatorType) {
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
    }

    private fun pushInt(i: Int) {
        _stack.add(StackElement(i))
    }

    private fun pushVariable(index: Int) {
        val variable = _variables[index]
                ?: throw IllegalArgumentException("No name for variable with index $index is found!")
        _stack.add(StackElement(variable.value))
    }
}

private fun <T> MutableList<T>.pop(): T? {
    if (this.isNotEmpty()) {
        return this.removeAt(this.size - 1)
    }

    return null
}