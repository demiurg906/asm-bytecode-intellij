/*
 *
 *  Copyright 2011 Cédric Champeau
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * /
 */

package org.objectweb.asm.idea

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.compiler.CompilerManager
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.impl.text.PsiAwareTextEditorImpl
import com.intellij.openapi.module.ModuleUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.CompilerModuleExtension
import com.intellij.openapi.util.Computable
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.psi.*
import com.intellij.psi.codeStyle.CodeStyleManager
import org.objectweb.asm.idea.config.ASMPluginComponent
import org.objectweb.asm.idea.insns.Insn
import org.objectweb.asm.idea.stackmachine.CommandsMap
import org.objectweb.asm.idea.stackmachine.StackMachineService
import org.objectweb.asm.idea.visitors.ClassInsnCollector
import org.objectweb.asm.idea.visitors.MethodTextifier
import reloc.org.objectweb.asm.ClassReader
import java.io.IOException
import java.io.PrintWriter
import java.io.StringWriter
import java.util.concurrent.Semaphore


/**
 * Given a java file (or any file which generates classes), tries to locate a .class file. If the compilation state is
 * not up to date, performs an automatic compilation of the class. If the .class file can be located, generates bytecode
 * instructions for the class and ASMified code, and displays them into a tool window.
 *
 * @author Cédric Champeau
 */
class ShowBytecodeOutlineAction : AnAction() {

    override fun update(e: AnActionEvent) {
        val virtualFile = e.getData(PlatformDataKeys.VIRTUAL_FILE)
        val project = e.getData(PlatformDataKeys.PROJECT)
        val presentation = e.presentation
        if (project == null || virtualFile == null) {
            presentation.isEnabled = false
            return
        }
        val psiFile = PsiManager.getInstance(project).findFile(virtualFile)
        presentation.isEnabled = psiFile is PsiClassOwner
    }

    override fun actionPerformed(e: AnActionEvent) {
        val virtualFile = e.getData(PlatformDataKeys.VIRTUAL_FILE)
        val project = e.getData(PlatformDataKeys.PROJECT)
        if (project == null || virtualFile == null) {
            return
        }

        val psiFile = PsiManager.getInstance(project).findFile(virtualFile)
        if (psiFile is PsiClassOwner) {
            val module = ModuleUtil.findModuleForPsiElement(psiFile)
            val cme = CompilerModuleExtension.getInstance(module!!)
            val compilerManager = CompilerManager.getInstance(project)
            val files = arrayOf(virtualFile)
            if ("class" == virtualFile.extension) {
                updateToolWindowContents(project, virtualFile)
            } else if (!virtualFile.isInLocalFileSystem && !virtualFile.isWritable) {
                // probably a source file in a library
                val psiClasses = psiFile.classes
                if (psiClasses.size > 0) {
                    updateToolWindowContents(project, psiClasses[0].originalElement.containingFile.virtualFile)
                }
            } else {
                val application = ApplicationManager.getApplication()
                application.runWriteAction { FileDocumentManager.getInstance().saveAllDocuments() }
                application.executeOnPooledThread {
                    val compileScope = compilerManager.createFilesCompileScope(files)
                    val result = arrayOf<VirtualFile?>(null)
                    val outputDirectories = cme?.getOutputRoots(true)
                    val semaphore = Semaphore(1)
                    try {
                        semaphore.acquire()
                    } catch (e1: InterruptedException) {
                        result[0] = null
                    }

                    if (outputDirectories != null && compilerManager.isUpToDate(compileScope)) {
                        application.invokeLater {
                            result[0] = findClassFile(outputDirectories, psiFile)
                            semaphore.release()
                        }
                    } else {
                        application.invokeLater {
                            compilerManager.compile(files) { aborted, errors, warnings, compileContext ->
                                if (errors == 0) {
                                    val outputDirectories = cme!!.getOutputRoots(true)
                                    if (outputDirectories != null) {
                                        result[0] = findClassFile(outputDirectories, psiFile)
                                    }
                                }
                                semaphore.release()
                            }
                        }
                        try {
                            semaphore.acquire()
                        } catch (e1: InterruptedException) {
                            result[0] = null
                        }

                    }
                    application.invokeLater { updateToolWindowContents(project, result[0]) }
                }
            }
        }
    }

    private fun findClassFile(outputDirectories: Array<VirtualFile>?, psiFile: PsiFile?): VirtualFile {
        return ApplicationManager.getApplication().runReadAction(object : Computable<VirtualFile> {
            override fun compute(): VirtualFile? {
                if (outputDirectories != null && psiFile is PsiClassOwner) {
                    val editor = FileEditorManager.getInstance(psiFile.project).getSelectedEditor(psiFile.virtualFile)
                    val caretOffset = if (editor == null) -1 else (editor as PsiAwareTextEditorImpl).editor.caretModel.offset
                    if (caretOffset >= 0) {
                        val psiClass = findClassAtCaret(psiFile, caretOffset)
                        if (psiClass != null) {
                            return getClassFile(psiClass)
                        }
                    }
                    val psiJavaFile = psiFile as PsiClassOwner?
                    for (psiClass in psiJavaFile!!.classes) {
                        val file = getClassFile(psiClass)
                        if (file != null) {
                            return file
                        }
                    }
                }
                return null
            }

            private fun getClassFile(psiClass: PsiClass): VirtualFile? {
                var psiClass = psiClass
                val sb = StringBuilder(psiClass.qualifiedName!!)
                while (psiClass.containingClass != null) {
                    sb.setCharAt(sb.lastIndexOf("."), '$')
                    psiClass = psiClass.containingClass as PsiClass
                }
                val classFileName = sb.toString().replace('.', '/') + ".class"
                for (outputDirectory in outputDirectories!!) {
                    val file = outputDirectory.findFileByRelativePath(classFileName)
                    if (file != null && file.exists()) {
                        return file
                    }
                }
                return null
            }

            private fun findClassAtCaret(psiFile: PsiFile, caretOffset: Int): PsiClass? {
                var elem = psiFile.findElementAt(caretOffset)
                while (elem != null) {
                    if (elem is PsiClass) {
                        return elem
                    }
                    elem = elem.parent
                }
                return null
            }
        })
    }

    /**
     * Reads the .class file, processes it through the ASM TraceVisitor and ASMifier to update the contents of the two
     * tabs of the tool window.
     *
     * @param project the project instance
     * @param file    the class file
     */
    private fun updateToolWindowContents(project: Project, file: VirtualFile?) {
        ApplicationManager.getApplication().runWriteAction(Runnable {
            if (file == null) {
                BytecodeOutline.getInstance(project).setCode(file, Constants.NO_CLASS_FOUND)
                ToolWindowManager.getInstance(project).getToolWindow("ASM").activate(null)
                return@Runnable
            }

            val (plainText, collectedInsns, lineNumbers) = getMethodsInfo(file, project)
            val service = StackMachineService.getInstance(project)
//            service.initializeClass(commandsMap)

            val bytecodeOutline = BytecodeOutline.getInstance(project)
            val psiFile = PsiFileFactory.getInstance(project!!).createFileFromText("asm.java", "asmified")
            CodeStyleManager.getInstance(project).reformat(psiFile)
            bytecodeOutline.setCode(file, plainText)
            ToolWindowManager.getInstance(project).getToolWindow("ASM").activate(null)
        })
    }

    data class MethodInfo(val plainText: String, val instructions: List<Insn>,
                          val lineNumbers: List<Int>)

    private fun getMethodsInfo(file: VirtualFile, project: Project): MethodInfo {
        var reader: ClassReader?
        try {
            file.refresh(false, false)
            reader = ClassReader(file.contentsToByteArray())
        } catch (e: IOException) {
            TODO("normal exception")
        }

        var flags = 0
        val config = project.getComponent(ASMPluginComponent::class.java)
        if (config.isSkipDebug)
            flags = flags or ClassReader.SKIP_DEBUG
        if (config.isSkipFrames)
            flags = flags or ClassReader.SKIP_FRAMES
        if (config.isExpandFrames)
            flags = flags or ClassReader.EXPAND_FRAMES
        if (config.isSkipCode) flags = flags or ClassReader.SKIP_CODE

        val visitor = ClassInsnCollector()
        reader.accept(visitor, flags)

        // internal visitor and printer
        val methodVisitor = visitor.methodVisitors[0]
        val methodPrinter= visitor.printers[0]

        // get string representation
        val stringWriter = StringWriter()
        val printWriter = PrintWriter(stringWriter)
        methodPrinter.print(printWriter)

        // indexes corresponding to lines of every instruction
        val lineNumbers = methodPrinter.lineNumbers

        // instructions of method
        val collectedInstructions = methodVisitor.collectedInstructions

        return MethodInfo(stringWriter.toString(), collectedInstructions, lineNumbers)
    }


}
