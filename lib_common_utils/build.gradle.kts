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

        multiDexEnabled = true
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true

        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
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
    compileOnly(Libraries.kotlinStdLib)
    implementation(Libraries.MultiDex.multiDex)

    coreLibraryDesugaring(Libraries.JavaSDK.Desugar.desugar)

    api(Libraries.AndroidX.Core.ktx)

    api(Libraries.Retrofit.retrofit)
    api(Libraries.Coroutine.core)

    api(Libraries.Dagger.android)
    api(Libraries.Dagger.androidSupport)

    compileOnly(Libraries.Material.material)

    kapt(Libraries.Dagger.compiler)
}