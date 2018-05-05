package org.objectweb.asm.idea.stackmachine

import org.objectweb.asm.idea.insns.Insn

//sealed class StackElement
data class StackElement(val value: Int)
//data class StackVariable(val name: String) : StackElement()

/**
 * Represents result of executing operation on stack.
 *
 * [removed] is a number of removed stack cells (for example, two for `add` or `multiply` instructions).
 *
 * [addedCells] are new stack cells (for example, result of for `multiply`); last element in this list is head in stack.
 */
data class StackOperationResult(val removed: Int, val addedCells: List<StackElement>)

interface StackMachine {
    companion object {
        fun getInstance(): StackMachine = StackMachineImpl()
    }

    val stack: List<StackElement>
    val localVariables: LocalVariableTable
    fun execute(insn: Insn): StackOperationResult
}
