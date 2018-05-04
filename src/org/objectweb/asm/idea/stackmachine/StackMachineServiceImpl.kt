package org.objectweb.asm.idea.stackmachine

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.LogicalPosition
import org.objectweb.asm.idea.ui.StackViewer

class StackMachineServiceImpl : StackMachineService {
    private var stackMachine: StackMachine = StackMachine.getInstance()
    private var commandsMap: CommandsMap = mapOf()

    private val stackViewer: StackViewer
        get() = _stackViewer!!
    private var _stackViewer: StackViewer? = null

    private val editor: Editor
        get() = _editor!!
    private var _editor: Editor? = null

    private var currentLine: Int = 0

    override fun initializeClass(map: CommandsMap) {
        stackMachine = StackMachine.getInstance()
        commandsMap = map
        stackViewer.stackMachine = stackMachine
    }

    override fun emulateMachineUntil() {
        val caretLine = editor.caretModel.currentCaret.logicalPosition.line
        for (line in currentLine..caretLine) {
            val command = commandsMap[line] ?: throw StackEvaluationException("no command under line $line")
            stackMachine.execute(command)
        }
        currentLine = caretLine
    }

    override fun emulateOneLine() {
        val command = commandsMap[currentLine] ?: throw StackEvaluationException("no command under line $currentLine")
        stackMachine.execute(command)
        moveCaretToNextLine()
        visualizeStack()
    }

    private fun visualizeStack() {
        stackViewer.updateStackView()
    }

    private fun moveCaretToNextLine() {
        val caret = editor.caretModel.currentCaret
        val currentPosition = caret.logicalPosition
        caret.moveToLogicalPosition(LogicalPosition(currentPosition.line + 1, 0))
        currentLine = caret.logicalPosition.line
    }

    override fun registerStackViewer(stackViewer: StackViewer) {
        this._stackViewer = stackViewer
    }

    override fun registerBytecodeEditor(editor: Editor) {
        _editor = editor
    }
}