package org.objectweb.asm.idea.ui

import com.intellij.ui.components.JBList
import javax.swing.*

class StackViewer {
    val component: JPanel
        get() = panel
    private val panel = JPanel()

    val stackItems = mutableListOf<String>("item1", "item2", "item3")
    var list: JBList<String>

    init {
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
        panel.add(JLabel("Stack state:"))
        list = JBList()
        initListComponent()
    }

    fun initListComponent() {
        list = JBList(stackItems)
        list.layoutOrientation = JList.VERTICAL
        panel.add(list)
    }

    fun removeLastItem() {
        panel.remove(list)
        stackItems.removeAt(stackItems.size - 1)
        initListComponent()
        SwingUtilities.updateComponentTreeUI(panel)
    }

    fun addNewItem(item: String) {
        panel.remove(list)
        stackItems.add(item)
        initListComponent()
        SwingUtilities.updateComponentTreeUI(panel)
    }

}
