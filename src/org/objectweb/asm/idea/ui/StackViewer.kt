package org.objectweb.asm.idea.ui

import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBList
import org.objectweb.asm.idea.stackmachine.StackMachine
import java.awt.GridLayout
import javax.swing.BoxLayout
import javax.swing.JList
import javax.swing.JPanel
import javax.swing.SwingUtilities

class StackViewer {
    var stackMachine: StackMachine = StackMachine.getInstance()

    val component: JPanel
        get() = globalPanel

    private val globalPanel = JPanel()

    private val stackList: JBList<String> = JBList()
    private val variablesList: JBList<String> = JBList()

    init {
        globalPanel.layout = GridLayout(1, 2)
        globalPanel.add(panelWithList(variablesList, "Variables:"))
        globalPanel.add(panelWithList(stackList, "Stack:"))
    }

    private fun panelWithList(list: JBList<*>, label: String): JPanel {
        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)

        panel.add(JBLabel(label))

        initListSettings(list)
        panel.add(list)
        return panel
    }

    private fun initListSettings(list: JBList<*>) {
        with (list) {
            layoutOrientation = JList.VERTICAL
            isEnabled = false
            dragEnabled = false
        }
    }

    fun updateStackView() {
        updateStackList()
        updateVariablesList()
        updateComponent()
    }

    private fun updateStackList() {
        val stack = stackMachine.stack.map { it.value.toString() }
        stackList.setListData(stack.toTypedArray())
    }

    private fun updateVariablesList() {
        val variables = stackMachine.localVariables.variables.map { "${it.name}: ${it.value}" }
        variablesList.setListData(variables.toTypedArray())
    }

    private fun updateComponent() = SwingUtilities.updateComponentTreeUI(globalPanel)
}
