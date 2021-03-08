object Kotlin {
    const val standardLibrary = "1.3.72"
}

object AndroidSDK {
    const val min = 19
    const val compile = 30
    const val target = compile
    const val buildToolsVersion = "30.0.3"
}

object AndroidClient {
    const val appId = "app.melon"
    const val versionCode = 1
    const val versionName = "1.0"
}

object BuildPlugins {

    object Versions {
        const val buildToolsVersion = "4.1.2"
        const val gradleVersion = "6.5.0"
    }

    const val androidGradlePlugin = "com.android.tools.build:gradle:${Versions.buildToolsVersion}"
    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Kotlin.standardLibrary}"
    const val androidApplication = "com.android.application"
    const val kotlinAndroid = "kotlin-android"
    const val kotlinKapt = "kotlin-kapt"
    const val kotlinAndroidExtensions = "kotlin-android-extensions"
}

object ScriptPlugins {
    const val infrastructure = "scripts.infrastructure"
    const val compilation = "scripts.compilation"
}

object Libraries {
    private object Versions {
        const val ktx = "1.2.0"
        const val appCompat = "1.1.0"
        const val material = "1.1.0"
        const val constraintLayout = "1.1.3"
    }

    const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Kotlin.standardLibrary}"
    const val ktxCore = "androidx.core:core-ktx:${Versions.ktx}"
    const val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
    const val material = "com.google.android.material:material:${Versions.material}"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
}