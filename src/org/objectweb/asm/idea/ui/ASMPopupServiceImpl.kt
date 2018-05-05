package org.objectweb.asm.idea.ui

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.MessageType
import com.intellij.openapi.ui.popup.Balloon
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.openapi.wm.WindowManager
import com.intellij.ui.awt.RelativePoint

class ASMPopupServiceImpl(private val project: Project) : ASMPopupService {
    override fun showOutOfMethodPopup() {
        showBalloon("Bytecode outline is allowed only in method body")
    }

    private fun showBalloon(message: String) {
        if (!project.isInitialized) {
            ApplicationManager.getApplication().invokeLater { showBalloon(message) }
            return
        }
        val statusBar = WindowManager.getInstance().getStatusBar(project)
        val balloon = JBPopupFactory.getInstance()
                .createHtmlTextBalloonBuilder(message, MessageType.WARNING, null)
                .setTitle("Error")
                .setHideOnClickOutside(true)
                .createBalloon()
        balloon.show(RelativePoint.getSouthEastOf(statusBar.component), Balloon.Position.atRight)
    }
}