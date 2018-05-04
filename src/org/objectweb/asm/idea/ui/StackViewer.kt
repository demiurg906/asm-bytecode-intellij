package org.objectweb.asm.idea.ui

import com.intellij.openapi.vcs.ui.FontUtil
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBList
import com.intellij.util.ui.JBEmptyBorder
import org.objectweb.asm.idea.stackmachine.StackMachine
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Component
import java.awt.GridLayout
import javax.swing.*
import javax.swing.border.LineBorder

class StackViewer {
    var stackMachine: StackMachine = StackMachine.getInstance()

    val component: JPanel
        get() = globalPanel

    private val globalPanel = JPanel()

    private val stackList: JBList<String> = JBList()
    private val variablesList: JBList<String> = JBList()

    init {
        with (globalPanel) {
            layout = GridLayout(1, 2)
            border = BorderFactory.createMatteBorder(1, 1, 0, 1, Color.BLACK)
            add(panelWithList(variablesList, "Variables:"))
            add(panelWithList(stackList, "Stack:"))
        }
    }

    private fun panelWithList(list: JBList<*>, labelText: String): JPanel {
        val panel = JPanel()
        panel.layout = BorderLayout()

        val label = JBLabel(labelText)
        label.border = JBEmptyBorder(1, 5, 1, 1)
        panel.add(label, BorderLayout.PAGE_START)

        initListSettings(list)
        panel.add(list, BorderLayout.CENTER)
        return panel
    }

    private fun initListSettings(list: JBList<*>) {
        with (list) {
            layoutOrientation = JList.VERTICAL
            isEnabled = true
            dragEnabled = false
            layout = BorderLayout()
            border = LineBorder.createBlackLineBorder()
            cellRenderer = MyListCellRenderer()
        }
    }

    private class MyListCellRenderer : DefaultListCellRenderer() {
        override fun getListCellRendererComponent(
                list: JList<*>?,
                value: Any,
                index: Int,
                isSelected: Boolean,
                cellHasFocus: Boolean
        ): Component {
            val label = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus) as JLabel
            label.font = FontUtil.getEditorFont()
            label.border = BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK)
            return label
        }
    }

    fun updateStackView() {
        updateStackList()
        updateVariablesList()
        updateComponent()
    }

    private fun updateStackList() {
        val stack = stackMachine.stack.map { it.value.toString() }
        stackList.setListData(stack.reversed().toTypedArray())
    }

    private fun updateVariablesList() {
        val variables = stackMachine.variables.values.map { "${it.name}: ${it.value}" }
        variablesList.setListData(variables.toTypedArray())
    }

    private fun updateComponent() = SwingUtilities.updateComponentTreeUI(globalPanel)
}
