package org.objectweb.asm.idea.ui

import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.project.Project

interface ASMPopupService {
    companion object {
        fun getInstance(project: Project): ASMPopupService = ServiceManager.getService(project, ASMPopupService::class.java)
    }

    fun showOutOfMethodPopup()
}