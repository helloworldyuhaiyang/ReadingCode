package com.helloworld.readingcode

import com.intellij.ui.components.JBScrollPane
import java.awt.Component
import javax.swing.*

class ShowCodeSnippetFrame(initialWeight: Int, initialHeight: Int, locationRelativeTo: Component?) :
    JFrameNoBorder(initialWeight, initialHeight, locationRelativeTo){

    init {
        val code = """
            package main
             
            import (
            	"fmt"
            	"github.com/garyburd/redigo/redis"
            )
             
            func main() {
            	c, err := redis.Dial("tcp", "127.0.0.1:6379")
            	if err != nil {
            		fmt.Println("conn redis fail ", err)
            		return
            	}
            	defer c.Close()
             
            	_, err = c.Do("Set", "abc", 100)
            	if err != nil {
            		fmt.Println("set fail", err)
            		return
            	}
             
            	// 将返回结果转为int类型
            	r, err := redis.Int(c.Do("Get", "abc"))
            	if err != nil {
            		fmt.Println("get fail", err)
            		return
            	}
            	fmt.Println(r)
            }
        """.trimIndent()

        val textArea = JTextArea(code)
        val scrollPane = JBScrollPane(textArea)

        this.addPanel(scrollPane)
    }
}