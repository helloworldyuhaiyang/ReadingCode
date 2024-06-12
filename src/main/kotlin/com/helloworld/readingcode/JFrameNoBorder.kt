package com.helloworld.readingcode

import com.intellij.ui.JBColor
import java.awt.*
import java.awt.event.*
import javax.swing.*

open class JFrameNoBorder(initialWidth: Int, initialHeight: Int, locationRelativeTo: Component?) : JFrame() {
    private var mouseX = 0
    private var mouseY = 0
    private var closeSize = 20
    private var isResizing = false
    private val resizeMargin = 10

    private val contentPanel = JPanel(null)

    init {
        isUndecorated = true
        defaultCloseOperation = DISPOSE_ON_CLOSE
        layout = BorderLayout()
        this.setSize(initialWidth, initialHeight)
        this.setLocationRelativeTo(locationRelativeTo)

        // set the window to always on top

        contentPanel.isOpaque = true

        // close button
        val closeButton = JButton("X")
        closeButton.font = Font("Arial", Font.BOLD, 12)
        closeButton.foreground = JBColor.GRAY
        closeButton.isFocusPainted = false
        closeButton.addActionListener {
            // 触发 windowClosing 事件
            dispatchEvent(WindowEvent(this, WindowEvent.WINDOW_CLOSING))
        }
        closeButton.setBounds(contentPanel.width - closeSize-5, 0, closeSize, closeSize)

        contentPanel.add(closeButton)
        this.add(contentPanel, BorderLayout.CENTER)

        // 添加组件监听器以在窗口大小发生变化时更新关闭按钮的位置
        this.addComponentListener(object : ComponentAdapter() {
            override fun componentResized(e: ComponentEvent) {
                closeButton.setBounds(contentPanel.width - closeSize-5, 0, closeSize, closeSize)
                for (component in contentPanel.components) {
                    if (component !== closeButton) {
                        component.setBounds(0, 0, contentPanel.width-closeSize-5, contentPanel.height-closeSize-5)
                    }
                }
            }
        })

        // 添加拖动和缩放功能
        val dragger = Dragger()
        this.addMouseListener(dragger)
        this.addMouseMotionListener(dragger)
    }

    // 新增方法，用于动态添加 JPanel
    fun addPanel(comp: JComponent) {
        comp.setBounds(0, 0, contentPanel.width-closeSize-5, contentPanel.height-closeSize-5)

        contentPanel.add(comp)
        contentPanel.revalidate()
        contentPanel.repaint()
    }

    private inner class Dragger : MouseAdapter() {
        override fun mousePressed(e: MouseEvent) {
            mouseX = e.x
            mouseY = e.y
            isResizing = isInResizeMargin(e)
        }

        override fun mouseDragged(e: MouseEvent) {
            if (isResizing) {
                val newWidth = e.x.coerceAtLeast(minimumSize.width)
                val newHeight = e.y.coerceAtLeast(minimumSize.height)
                setSize(newWidth, newHeight)
            } else {
                val x = e.xOnScreen - mouseX
                val y = e.yOnScreen - mouseY
                setLocation(x, y)
            }
        }

        override fun mouseMoved(e: MouseEvent) {
            cursor = if (isInResizeMargin(e)) {
                Cursor(Cursor.SE_RESIZE_CURSOR)
            } else {
                Cursor(Cursor.DEFAULT_CURSOR)
            }
        }

        private fun isInResizeMargin(e: MouseEvent): Boolean {
            val width = this@JFrameNoBorder.width
            val height = this@JFrameNoBorder.height
            return e.x >= width - resizeMargin && e.y >= height - resizeMargin
        }
    }
}

