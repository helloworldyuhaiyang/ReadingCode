plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.23"
    id("org.jetbrains.intellij") version "1.17.2"
}

group = "com.helloworld"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("2024.1.2")
    type.set("IC") // Target IDE Platform

    plugins.set(listOf("org.jetbrains.kotlin"))
}

tasks {
    runIde {
        jvmArgs = listOf("-Xmx4G")
        systemProperty("ide.experimental.ui", "true")
        systemProperty("ide.show.tips.on.startup.default.value", false)
        systemProperty("idea.trust.all.projects", true)
        systemProperty("jb.consents.confirmation.enabled", false)
        systemProperty("ide.browser.jcef.enabled", true)
        systemProperty("ide.browser.jcef.headless.enabled", true)
        systemProperty("ide.browser.jcef.testMode.enabled", true)
        systemProperty("ide.browser.jcef.contextMenu.devTools.enabled", true)
    }

    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

    patchPluginXml {
        sinceBuild.set("232")
        untilBuild.set("242.*")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}
