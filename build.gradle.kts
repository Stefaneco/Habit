buildscript {
    extra.apply {
        set("composeVersion", "1.4.0-alpha02")
        set("roomVersion", "2.5.0")
        set("hiltVersion", "2.44")
    }
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.21")
        classpath("com.android.tools.build:gradle:7.2.2")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.44")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}