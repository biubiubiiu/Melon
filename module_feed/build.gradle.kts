plugins {
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

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles("proguard-rules.pro")
        }
    }

    buildFeatures {
        viewBinding = true
    }
}

kapt {
    correctErrorTypes = true
    useBuildCache = true
}

dependencies {
    compileOnly(project(Modules.baseFramework))
    compileOnly(project(Modules.dataAndroid))

    compileOnly(project(Modules.userApi))
    compileOnly(project(Modules.poiApi))
    compileOnly(project(Modules.comment))

    implementation(Libraries.Fragment.fragment)
    implementation(Libraries.Fragment.ktx)

    kapt(Libraries.Epoxy.compiler)
    kapt(Libraries.Dagger.compiler)
    kapt(Libraries.Dagger.processor)
}