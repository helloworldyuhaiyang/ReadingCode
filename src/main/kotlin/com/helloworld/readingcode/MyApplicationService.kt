package com.helloworld.readingcode

interface MyApplicationService {
    fun performServiceAction()
    fun setBrowserWindow(window: BrowserWindow)
    fun getBrowserWindow(): BrowserWindow
}
