package org.objectweb.asm.idea.stackmachine

import org.objectweb.asm.idea.insns.*
import reloc.org.objectweb.asm.Label

class StackMachineImpl(override val localVariables: LocalVariableTable, val labelMap: LabelMap) : StackMachine {
    private val _stack = mutableListOf<StackElement>()
    override val stack: List<StackElement>
        get() = _stack.toList()

    override fun execute(instruction: Instruction): StackOperationResult {
        return when (instruction) {
            is IntConst -> pushInt(instruction.operand)
            is LocalLoad -> pushVariable(instruction.index)
            is LocalStore -> storeVariable(instruction.index)
            is BinaryOperation -> executeBinaryOperation(instruction.op)
            is IntCompareJump -> intJump(instruction.comparatorType, instruction.target)
            is Goto -> gotoJump(instruction.target)
            else -> TODO("$instruction is not handled yet.")
        }
    }

    private fun gotoJump(label: Label): StackOperationResult {
        return StackOperationResult(removed = 0, addedCells = emptyList(), nextLine = labelMap[label])
    }

    private fun intJump(cmp: ComparatorType, label: Label): StackOperationResult {
        val right = (_stack.pop()?.value)
                ?: throw IllegalArgumentException("No first argument for operation $cmp.")
        val left = (_stack.pop()?.value)
                ?: throw IllegalArgumentException("No second argument for operation $cmp.")

        var nextLine: Int? = null
        var targetLine = labelMap[label]
        when (cmp) {
            ComparatorType.LESS -> {
                if (left < right) {
                    nextLine = targetLine
                }
            }
            ComparatorType.LESS_EQUAL -> {
                if (left <= right) {
                    nextLine = targetLine
                }
            }

            ComparatorType.GREATER -> {
                if (left > right) {
                    nextLine = targetLine
                }
            }

            ComparatorType.GREATER_EQUAL -> {
                if (left >= right) {
                    nextLine = targetLine
                }
            }

            ComparatorType.EQUAL -> {
                if (left == right) {
                    nextLine = targetLine
                }
            }

            ComparatorType.NOT_EQUAL -> {
                if (left != right) {
                    nextLine = targetLine
                }
            }

        }
        return StackOperationResult(removed = 2, addedCells = emptyList(), nextLine = nextLine)
    }

    private fun executeBinaryOperation(op: OperatorType): StackOperationResult {
        val right = (_stack.pop()?.value)
                ?: throw IllegalArgumentException("No first argument for operation $op.")
        val left = (_stack.pop()?.value)
                ?: throw IllegalArgumentException("No second argument for operation $op.")

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
        localVariables.setVariableById(index, value)

        return StackOperationResult(removed = 1, addedCells = emptyList())
    }

    private fun pushVariable(index: Int): StackOperationResult {
        val variable = localVariables.findVariableById(index)
                ?: throw IllegalArgumentException("No name for variable with index $index is found!")

        return pushInt(variable.value)
    }

    override fun resetState() {
        _stack.clear()
        localVariables.resetState()
    }
}

private fun <T> MutableList<T>.pop(): T? {
    if (this.isNotEmpty()) {
        return this.removeAt(this.size - 1)
    }

    return null
}