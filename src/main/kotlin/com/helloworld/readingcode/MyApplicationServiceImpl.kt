package com.helloworld.readingcode

import com.intellij.openapi.components.Service
import java.net.HttpURLConnection
import java.net.URI


@Service
class MyApplicationServiceImpl : MyApplicationService {
    private lateinit var browserWindow: BrowserWindow

    override fun performServiceAction() {
        // 服务的具体实现
    }

    override fun setBrowserWindow(window: BrowserWindow) {
        browserWindow = window
    }

    override fun getBrowserWindow(): BrowserWindow {
        return browserWindow
    }

}