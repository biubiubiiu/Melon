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
    api(project(Modules.data))

    api(Libraries.AndroidX.Core.ktx)
    api(Libraries.AndroidX.AppCompat.appCompat)
    api(Libraries.RecyclerView.recyclerView)
    api(Libraries.Material.material)
    api(Libraries.Transition.transition)
    api(Libraries.Transition.ktx)
    api(Libraries.SwipeRefreshLayout.swipeRefreshLayout)

    api(Libraries.Room.runtime)
    api(Libraries.Dagger.androidSupport)

    api(Libraries.Mavericks.mavericks)
    api(Libraries.Epoxy.runtime)
    api(Libraries.Epoxy.paging)

    api(Libraries.Coil.coil)

    api(Libraries.OkHttp.okHttp)

    kapt(Libraries.Epoxy.compiler)
}