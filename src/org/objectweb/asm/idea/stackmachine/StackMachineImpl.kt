package org.objectweb.asm.idea.stackmachine

import org.objectweb.asm.idea.insns.*
import reloc.org.objectweb.asm.Label

class StackMachineImpl(override val localVariables: LocalVariableTable, val labelMap: LabelMap) : StackMachine {
    private val _stack = mutableListOf<StackElement>()
    override val stack: List<StackElement>
        get() = _stack.toList()

    override fun execute(insn: Insn): StackOperationResult {

        return when (insn) {
            is IntConst -> pushElement(IntValue(insn.operand))
            is LongConst -> pushElement(LongValue(insn.operand))
            is FloatConst -> pushElement(FloatValue(insn.operand))
            is DoubleConst -> pushElement(DoubleValue(insn.operand))

            is LocalLoad -> pushVariable(insn.index, insn.type)
            is LocalStore -> storeVariable(insn.index, insn.type)

            is BinaryOperation -> executeBinaryOperation(insn)

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

    private fun executeBinaryOperation(operation: BinaryOperation): StackOperationResult {

        val element = when (operation.type) {
            PrimitiveType.INT -> IntValue(performOperation(operation.op, IntOperations))
            PrimitiveType.LONG -> LongValue(performOperation(operation.op, LongOperations))
            PrimitiveType.FLOAT -> FloatValue(performOperation(operation.op, FloatOperations))
            PrimitiveType.DOUBLE -> DoubleValue(performOperation(operation.op, DoubleOperations))
        }

        _stack.add(element)

        return StackOperationResult(removed = 2, addedCells = listOf(element))
    }

    private inline fun <reified T : Number> performOperation(operation: OperatorType, operations: Operations<T>): T {
        val right = extractNumber<T>()
        val left = extractNumber<T>()

        return when (operation) {
            OperatorType.ADD -> operations.add(left, right)
            OperatorType.SUBTRACT -> operations.subtract(left, right)
            OperatorType.MULTIPLY -> operations.multiply(left, right)
            OperatorType.DIVIDE -> operations.divide(left, right)
            OperatorType.REMAINDER -> operations.reminder(left, right)
        }
    }

    private inline fun <reified T : Number> extractNumber(): T {
        if (_stack.last().value is T) {
            return _stack.pop()!!.value as T
        }

        throw IllegalArgumentException(
                "Cannot extractNumber ${T::class}; last element of stack has type ${_stack.last()::class}"
        )
    }

    private fun pushInt(i: Int): StackOperationResult {
        _stack.add(StackElement(i))
        return StackOperationResult(removed = 0, addedCells = _stack.takeLast(1))
    }

    private fun storeVariable(index: Int, type: PrimitiveType): StackOperationResult {
        val value = _stack.pop()?.value
                ?: throw IllegalArgumentException("No elements on stack to store in variable $index.")

        when (type) {
            PrimitiveType.INT -> localVariables.setVariableById(index, value as Int)
            PrimitiveType.LONG -> localVariables.setVariableById(index, value as Long)
            PrimitiveType.FLOAT -> localVariables.setVariableById(index, value as Float)
            PrimitiveType.DOUBLE -> localVariables.setVariableById(index, value as Double)
        }

        return StackOperationResult(removed = 1, addedCells = emptyList())
    }

    private fun pushVariable(index: Int, type: PrimitiveType): StackOperationResult {
        val variable = localVariables.findVariableById(index)
                ?: throw IllegalArgumentException("No name for variable with index $index is found!")

        val element = when (type) {
            PrimitiveType.INT -> IntValue(variable.value as Int)
            PrimitiveType.LONG -> LongValue(variable.value as Long)
            PrimitiveType.FLOAT -> FloatValue(variable.value as Float)
            PrimitiveType.DOUBLE -> DoubleValue(variable.value as Double)
        }

        return pushElement(element)
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