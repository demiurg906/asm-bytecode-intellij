package org.objectweb.asm.idea.actions

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import org.objectweb.asm.idea.stackmachine.StackMachineService

class StartNewStackAction : AnAction("Init stack", "", AllIcons.General.Run) {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val service = StackMachineService.getInstance(project)
        ApplicationManager.getApplication().invokeLater {
            service.emulateMachineUntil()
        }
    }

    override fun displayTextInToolbar(): Boolean = true
}

class EmulateLineAction : AnAction("Emulate line", "", AllIcons.Actions.StepOut) {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val service = StackMachineService.getInstance(project)
        ApplicationManager.getApplication().invokeLater {
            service.emulateOneLine()
        }
    }

    override fun displayTextInToolbar(): Boolean = true
}