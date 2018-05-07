package org.objectweb.asm.idea.actions

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import org.objectweb.asm.idea.stackmachine.StackMachineService

fun serviceFromAction(e: AnActionEvent): StackMachineService? {
    val project = e.project ?: return null
    return StackMachineService.getInstance(project)
}

class EmulateToCursorAction : AnAction("Emulate to cursor", "", AllIcons.General.Run) {
    override fun actionPerformed(e: AnActionEvent) {
        val service = serviceFromAction(e) ?: return
        ApplicationManager.getApplication().invokeLater {
            service.emulateToCursor()
        }
    }

    override fun displayTextInToolbar(): Boolean = true
}

class EmulateLineAction : AnAction("Emulate line", "", AllIcons.Actions.StepOut) {
    override fun actionPerformed(e: AnActionEvent) {
        val service = serviceFromAction(e) ?: return
        ApplicationManager.getApplication().invokeLater {
            service.emulateOneLine()
        }
    }

    override fun displayTextInToolbar(): Boolean = true
}

class ResetStackAction : AnAction("Reset stack", "", AllIcons.Actions.Refresh) {
    override fun actionPerformed(e: AnActionEvent) {
        val service = serviceFromAction(e) ?: return
        ApplicationManager.getApplication().invokeLater {
            service.resetStack()
        }
    }
}