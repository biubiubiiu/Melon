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
    implementation(project(Modules.accountApi))

    implementation(project(Modules.baseFramework))
    implementation(project(Modules.dataAndroid))

    implementation(Libraries.Fragment.fragment)
    implementation(Libraries.Fragment.ktx)
    implementation(Libraries.Mavericks.mavericks)
//    implementation("androidx.annotation:annotation:1.1.0")
//    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.3.1")
//    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1")

    kapt(Libraries.Epoxy.compiler)
    kapt(Libraries.Dagger.compiler)
    kapt(Libraries.Dagger.processor)

    kapt(Libraries.Router.compiler)
}