package org.objectweb.asm.idea.stackmachine

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.LogicalPosition
import org.objectweb.asm.idea.insns.Insn
import org.objectweb.asm.idea.ui.StackViewer

class StackMachineServiceImpl : StackMachineService {
    override fun initializeClass(map: CommandsMap) {
        _stackMachine = StackMachine.getInstance()
        commandsMap = map
    }

    private var _stackMachine: StackMachine = StackMachine.getInstance()
    private var commandsMap: CommandsMap = sortedMapOf()
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
            commandsMap[line]?.executeOnStack()
        }
    }

    override fun emulateOneLine() {
        commandsMap[currentLine]?.executeOnStack()

        editor.moveCaretToNextLine()
    }

    private fun Insn.executeOnStack() = _stackMachine.execute(this)

    private fun Editor.moveCaretToNextLine() {
        caretModel.currentCaret.moveToLogicalPosition(LogicalPosition(currentLine + 1, 0))
    }

    override fun registerStackViewer(stackViewer: StackViewer) {
        this._stackViewer = stackViewer
    }

    override fun registerBytecodeEditor(editor: Editor) {
        _editor = editor
    }
}