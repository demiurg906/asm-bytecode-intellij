package org.objectweb.asm.idea.stackmachine

import org.objectweb.asm.idea.insns.Instruction

sealed class StackElement(open val value: Number)
data class IntValue(override val value: Int): StackElement(value)
data class LongValue(override val value: Long): StackElement(value)
data class FloatValue(override val value: Float): StackElement(value)
data class DoubleValue(override val value: Double): StackElement(value)
//data class StackVariable(val name: String) : StackElement()

/**
 * Represents result of executing operation on stack.
 *
 * [removed] is a number of removed stack cells (for example, two for `add` or `multiply` instructions).
 *
 * [addedCells] are new stack cells (for example, result of for `multiply`); last element in this list is head in stack.
 */
data class StackOperationResult(val removed: Int, val addedCells: List<StackElement>, val nextLine: Int? = null)

interface StackMachine {
    companion object {
        fun getInstance(localVariables: LocalVariableTable = LocalVariableTable.emptyTable, labelMap: LabelMap = emptyMap()) = StackMachineImpl(localVariables, labelMap)
    }

    val stack: List<StackElement>
    val localVariables: LocalVariableTable
    fun execute(insn: Instruction): StackOperationResult

    /**
     * Resets all variables in stack's local variable table and clears stack.
     */
    fun resetState()
}
