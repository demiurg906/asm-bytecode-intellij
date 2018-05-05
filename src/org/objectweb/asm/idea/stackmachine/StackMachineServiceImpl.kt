package org.objectweb.asm.idea.stackmachine

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.LogicalPosition
import org.objectweb.asm.idea.insns.Insn
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

    private val currentLine: Int
        get() = editor.caretModel.currentCaret.logicalPosition.line

    private var lastExecutedLine: Int = 0

    override fun initializeClass(params: StackParams) {
        stackMachine = StackMachine.getInstance(params.localVariables)
        commandsMap = params.commandsMap
        stackViewer.stackMachine = stackMachine
        visualizeStack()
    }

    override fun resetStack() {
        stackMachine = StackMachine.getInstance()
        stackViewer.stackMachine = stackMachine
        lastExecutedLine = 0
        visualizeStack()
    }

    override fun emulateToCursor() {
        for (line in lastExecutedLine until currentLine) {
            commandsMap[line]?.executeOnStack()
        }
        lastExecutedLine = currentLine
        visualizeStack()
    }

    override fun emulateOneLine() {
        lastExecutedLine = currentLine
        commandsMap[currentLine]?.executeOnStack().run { visualizeStack() }
        moveCaretToNextLine()
    }

    private fun Insn.executeOnStack() = stackMachine.execute(this)

    private fun visualizeStack() {
        stackViewer.updateStackView()
    }

    private fun moveCaretToNextLine() {
        editor.caretModel.currentCaret.moveToLogicalPosition(LogicalPosition(currentLine + 1, 0))
    }

    override fun registerStackViewer(stackViewer: StackViewer) {
        this._stackViewer = stackViewer
    }

    override fun registerBytecodeEditor(editor: Editor) {
        _editor = editor
    }
}