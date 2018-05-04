package org.objectweb.asm.idea.stackmachine

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.LogicalPosition
import org.objectweb.asm.idea.ui.StackViewer

class StackMachineServiceImpl : StackMachineService {
    override fun initializeClass(map: CommandsMap) {
        _stackMachine = StackMachine.getInstance()
        commandsMap = map
    }

    private var _stackMachine: StackMachine = StackMachine.getInstance()
    private var commandsMap: CommandsMap = mapOf()
    override val stackViewer: StackViewer
        get() = _stackViewer!!
    private var _stackViewer: StackViewer? = null

    private val editor: Editor
        get() = _editor!!
    private var _editor: Editor? = null

    override val currentLine: Int
        get() = editor.caretModel.logicalPosition.line

    override val stackMachine: StackMachine
        get() = _stackMachine

    override fun emulateMachineUntil() {
        for (line in 0 until currentLine) {
            val command = commandsMap[line] ?: throw StackEvaluationException("no command under line $line")
            _stackMachine.execute(command)
        }
    }

    override fun emulateOneLine() {
        val command = commandsMap[currentLine] ?: throw StackEvaluationException("no command under line $currentLine")
        _stackMachine.execute(command)
        moveCaretToNextLine()
    }

    private fun moveCaretToNextLine() {
        val caret = editor.caretModel.currentCaret
        val currentPosition = caret.logicalPosition
        caret.moveToLogicalPosition(LogicalPosition(currentPosition.line + 1, currentPosition.column))
    }

    override fun registerStackViewer(stackViewer: StackViewer) {
        this._stackViewer = stackViewer
    }

    override fun registerBytecodeEditor(editor: Editor) {
        _editor = editor
    }
}