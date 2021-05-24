// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        jcenter()
        mavenCentral()
    }
    dependencies {
        classpath(BuildPlugins.androidGradlePlugin)
        classpath(BuildPlugins.kotlinGradlePlugin)

        classpath(BuildPlugins.benchmark)
        classpath(BuildPlugins.AndResGuard.AndResGuard)

        classpath(BuildPlugins.ByteX.byteX)
        classpath(BuildPlugins.ByteX.byteX_const_inline)

        classpath(FirebaseService.googleServicePlugin)
        classpath(FirebaseService.crashlyticsPlugin)
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven("https://jitpack.io")
        mavenCentral()
    }
}

tasks {
    val clean by registering(Delete::class) {
        delete(buildDir)
    }
}