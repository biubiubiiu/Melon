plugins {
    // Library Specific Plugins
    id(BuildPlugins.androidLibrary)
    id(BuildPlugins.kotlinAndroid)
    id(BuildPlugins.kotlinKapt)

    id(ScriptPlugins.infrastructure)
}

android {
    compileSdkVersion(AndroidSDK.compile)
    buildToolsVersion(AndroidSDK.buildToolsVersion)

    defaultConfig {
        minSdkVersion(AndroidSDK.min)
        targetSdkVersion(AndroidSDK.target)

        versionCode = AndroidClient.versionCode
        versionName = AndroidClient.versionName
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles("proguard-rules.pro")
        }
    }
}

kapt {
    correctErrorTypes = true
    useBuildCache = true
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    compileOnly(project(Modules.commonUtils))

    compileOnly(Libraries.kotlinStdLib)
    compileOnly(Libraries.Dagger.dagger)
    kapt(Libraries.Dagger.compiler)
}