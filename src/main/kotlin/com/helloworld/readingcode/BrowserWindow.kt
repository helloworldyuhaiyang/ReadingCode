package com.helloworld.readingcode

import com.intellij.openapi.application.ApplicationActivationListener
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.IdeFrame
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.openapi.wm.ex.ToolWindowManagerListener
import com.intellij.openapi.wm.ex.WindowManagerEx
import com.intellij.ui.jcef.JBCefBrowser
import com.intellij.ui.jcef.JBCefBrowserBuilder
import com.intellij.ui.content.ContentFactory
import java.awt.BorderLayout
import java.awt.event.FocusAdapter
import java.awt.event.FocusEvent
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.JTextField

class BrowserWindow : ToolWindowFactory {
    private lateinit var browser: JBCefBrowser
    private var defaultURL = "https://www.baidu.com"
    private var zoomLevel = 0.0
    private val codeSnippetFrames = mutableListOf<ShowCodeSnippetFrame>()

    fun getWeight(): Int {
        return browser.component.width
    }

    fun getHeight(): Int {
        return browser.component.height
    }

    fun getBrowser(): JBCefBrowser {
        return browser
    }

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val panel = JPanel(BorderLayout())
        val urlPanel = JPanel(BorderLayout())
        val urlField = JTextField(defaultURL)
        val goButton = JButton("Go")
        val zoomInButton = JButton("+")
        val zoomOutButton = JButton("-")
        val zoomResetButton = JButton("=")

        browser = JBCefBrowserBuilder().setUrl(defaultURL).build()

        goButton.addActionListener {
            val url = urlField.text
            if (url.isNotEmpty()) {
                browser.loadURL(url)
            }
        }

        zoomInButton.addActionListener {
            zoomLevel += 0.1
            browser.cefBrowser.zoomLevel = zoomLevel
        }

        zoomOutButton.addActionListener {
            zoomLevel -= 0.1
            browser.cefBrowser.zoomLevel = zoomLevel
        }

        zoomResetButton.addActionListener {
            zoomLevel = 0.0
            browser.cefBrowser.zoomLevel = zoomLevel
        }

        val addSnippetButton = JButton("code")
        addSnippetButton.addActionListener {
            val snippetFrame = ShowCodeSnippetFrame(this.getWeight(), this.getHeight() / 4, browser.component)
            codeSnippetFrames.add(snippetFrame)
            snippetFrame.addWindowListener(object : WindowAdapter() {
                override fun windowClosing(e: WindowEvent) {
                    codeSnippetFrames.remove(snippetFrame)
                }
            })
            snippetFrame.show()
        }

        val buttonPanel = JPanel()
        buttonPanel.add(goButton)
        buttonPanel.add(zoomInButton)
        buttonPanel.add(zoomOutButton)
        buttonPanel.add(zoomResetButton)
        buttonPanel.add(addSnippetButton)

        urlPanel.add(urlField, BorderLayout.CENTER)
        urlPanel.add(buttonPanel, BorderLayout.EAST)

        panel.add(urlPanel, BorderLayout.NORTH)
        panel.add(browser.component, BorderLayout.CENTER)

        val contentFactory = ContentFactory.getInstance()
        val content = contentFactory.createContent(panel, "", false)
        toolWindow.contentManager.addContent(content)

        // 获取 MyApplicationService 实例，并设置 BrowserWindow
        val server = ApplicationManager.getApplication().getService(MyApplicationService::class.java)
        server.setBrowserWindow(this)

        // update visible of snippet frames
        project.messageBus.connect().subscribe(ToolWindowManagerListener.TOPIC, object : ToolWindowManagerListener {
            override fun stateChanged(toolWindowManager: ToolWindowManager) {
                val readingCodeWindow = toolWindowManager.getToolWindow("ReadingCodeBrowser")
                if (readingCodeWindow != null) {
                    val isVisible = readingCodeWindow.isVisible
                    toggleSnippetFrames(isVisible)
                }
            }
        })
    }
    private fun toggleSnippetFrames(isVisible: Boolean) {
        for (frame in codeSnippetFrames) {
            frame.isVisible = isVisible
        }
    }
}
