package org.objectweb.asm.idea.stackmachine

import org.objectweb.asm.idea.insns.*

class StackMachineImpl(override val localVariables: LocalVariableTable) : StackMachine {
    private val _stack = mutableListOf<StackElement>()

    override val stack: List<StackElement>
        get() = _stack.toList()

    override fun execute(insn: Insn): StackOperationResult {

        return when (insn) {
            is IntConst -> pushElement(IntValue(insn.operand))
            is LongConst -> pushElement(LongValue(insn.operand))
            is FloatConst -> pushElement(FloatValue(insn.operand))
            is DoubleConst -> pushElement(DoubleValue(insn.operand))

            is LocalLoad -> pushVariable(insn.index)
            is LocalStore -> storeVariable(insn.index)

            is BinaryOperation -> executeBinaryOperation(insn)

            else -> TODO("$insn is not handled yet.")
        }
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

    private fun pushElement(element: StackElement): StackOperationResult {
        _stack.add(element)
        return StackOperationResult(removed = 0, addedCells = _stack.takeLast(1))
    }

    private fun storeVariable(index: Int): StackOperationResult {
        val value = _stack.pop()?.value
                ?: throw IllegalArgumentException("No elements on stack to store in variable $index.")
        localVariables.setVariableById(index, value as Int)

        return StackOperationResult(removed = 1, addedCells = emptyList())
    }

    private fun pushVariable(index: Int): StackOperationResult {
        val variable = localVariables.findVariableById(index)
                ?: throw IllegalArgumentException("No name for variable with index $index is found!")

        return pushElement(IntValue(variable.value as Int))
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