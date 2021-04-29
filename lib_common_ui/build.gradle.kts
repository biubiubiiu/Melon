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

    buildFeatures {
        viewBinding = true
    }
}

kapt {
    correctErrorTypes = true
    useBuildCache = true
}

dependencies {
    compileOnly(project(Modules.data))

    compileOnly(Libraries.AndroidX.Core.ktx)

    api(Libraries.Navigation.fragmentKtx)
    api(Libraries.Navigation.uiKtx)

    api(Libraries.ConstraintLayout.constraintLayout)
    api(Libraries.RecyclerView.recyclerView)
    api(Libraries.Material.material)
    api(Libraries.Transition.transition)
    api(Libraries.Transition.ktx)
    api(Libraries.SwipeRefreshLayout.swipeRefreshLayout)
    api(Libraries.PhotoView.photoView)
    api(Libraries.Coil.coil)
    api(Libraries.Lottie.lottie)
    api(Libraries.Shimmer.shimmer)

    api(Libraries.Epoxy.runtime)
    kapt(Libraries.Epoxy.compiler)
}