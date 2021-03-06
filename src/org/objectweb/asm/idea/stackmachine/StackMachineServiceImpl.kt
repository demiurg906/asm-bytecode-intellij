package org.objectweb.asm.idea.stackmachine

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.LogicalPosition
import org.objectweb.asm.idea.insns.Instruction
import org.objectweb.asm.idea.ui.StackViewer

class StackMachineServiceImpl : StackMachineService {
    private var stackMachine: StackMachine = StackMachine.getInstance()
    private var commandsMap: CommandsMap = mapOf()

    override val stackViewer: StackViewer = StackViewer()

    private val editor: Editor
        get() = _editor!!
    private var _editor: Editor? = null

    private val currentLine: Int
        get() = editor.caretModel.currentCaret.logicalPosition.line

    private var lastExecutedLine: Int = 0

    override fun initializeClass(params: StackParams) {
        stackMachine = StackMachine.getInstance(params.localVariables, params.labelToLineMap)
        commandsMap = params.commandsMap
        stackViewer.stackMachine = stackMachine
        visualizeStack()
    }

    override fun resetStack() {
        stackMachine.resetState()
        lastExecutedLine = 0
        visualizeStack()
    }

    override fun emulateToCursor() {
        while (lastExecutedLine != currentLine) {
            val nextLine = commandsMap[lastExecutedLine]?.executeOnStack()?.nextLine
            if (nextLine == null) {
                lastExecutedLine += 1
            } else {
                lastExecutedLine = nextLine
            }
        }
        visualizeStack()
    }

    override fun emulateOneLine() {
        lastExecutedLine = currentLine
        val operationResult: StackOperationResult? = commandsMap[currentLine]?.executeOnStack()
        if (operationResult != null) {
            visualizeStack()
        }
        val nextLine: Int? = operationResult?.nextLine
        moveCaretToNextLine(nextLine)
    }

    private fun Instruction.executeOnStack() = stackMachine.execute(this)

    private fun visualizeStack() {
        stackViewer.updateStackView()
    }

    private fun moveCaretToNextLine(line: Int?) {
        val nextLine = line ?: currentLine + 1
        editor.caretModel.currentCaret.moveToLogicalPosition(LogicalPosition(nextLine, 0))
    }

    override fun registerBytecodeEditor(editor: Editor) {
        _editor = editor
    }
}