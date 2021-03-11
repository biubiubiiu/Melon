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
        const val butterKnifeVersion = "10.2.3"
    }

    const val androidGradlePlugin = "com.android.tools.build:gradle:${Versions.buildToolsVersion}"
    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Kotlin.standardLibrary}"
    const val androidApplication = "com.android.application"
    const val androidLibrary = "com.android.library"
    const val kotlin = "kotlin"
    const val kotlinAndroid = "kotlin-android"
    const val kotlinKapt = "kotlin-kapt"
    const val kotlinAndroidExtensions = "kotlin-android-extensions"
}

object ScriptPlugins {
    const val infrastructure = "scripts.infrastructure"
    const val compilation = "scripts.compilation"
}

object Modules {
    const val baseFramework = ":lib_base_framework"
    const val base = ":base"
    const val data = ":data"
    const val dataAndroid = ":data-android"
    const val domain = ":domain"
    const val recommend = ":ui-recommend"
    const val follow = ":ui-following"
}

object Libraries {
    private object Versions {
        const val ktx = "1.2.0"
        const val appCompat = "1.1.0"
        const val material = "1.3.0"
        const val constraintLayout = "2.0.4"
        const val recyclerView = "1.1.0"
        const val multiDex = "2.0.1"
        const val navigation = "2.2.2"
        const val lifecycle = "2.3.0"
        const val coil = "1.1.1"
        const val epoxy = "4.4.2"
        const val mavericks = "2.1.0"
        const val paging = "3.0.0-alpha13"
        const val room = "2.3.0-alpha04"
        const val gson = "2.8.6"
        const val okHttp = "4.9.0"
        const val retrofit = "2.9.0"
        const val coroutine = "1.3.9"
        const val store = "4.0.0"
        const val dagger = "2.28"
        const val fragment = "1.3.0-beta02"
    }

    const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Kotlin.standardLibrary}"
    const val ktxCore = "androidx.core:core-ktx:${Versions.ktx}"
    const val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
    const val material = "com.google.android.material:material:${Versions.material}"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    const val recyclerView = "androidx.recyclerview:recyclerview:${Versions.recyclerView}"

    const val multiDex = "androidx.multidex:multidex:${Versions.multiDex}"

    // Jetpack Navigation
    const val navigationFragment = "androidx.navigation:navigation-fragment:${Versions.navigation}"
    const val navigationUI = "androidx.navigation:navigation-ui:${Versions.navigation}"
    const val navigationFragmentKtx = "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
    const val navigationUIKtx = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"

    // Lifecycle
    const val lifecycleLivedataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle}"
    const val lifecycleViewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"

    // Coil
    const val coil = "io.coil-kt:coil:${Versions.coil}"

    // Epoxy
    const val epoxyRuntime = "com.airbnb.android:epoxy:${Versions.epoxy}"
    const val epoxyCompiler = "com.airbnb.android:epoxy-processor:${Versions.epoxy}"
    const val epoxyPaging = "com.airbnb.android:epoxy-paging3:${Versions.epoxy}"

    // Mavericks
    const val mavericks = "com.airbnb.android:mavericks:${Versions.mavericks}"

    // Paging3
    const val paging3Common = "androidx.paging:paging-common:${Versions.paging}"
    const val paging3 = "androidx.paging:paging-runtime:${Versions.paging}"

    // Room
    const val roomCommon = "androidx.room:room-common:${Versions.room}"
    const val roomRuntime = "androidx.room:room-runtime:${Versions.room}"
    const val roomCompiler = "androidx.room:room-compiler:${Versions.room}"
    const val roomKtx = "androidx.room:room-ktx:${Versions.room}"

    // Gson
    const val gson = "com.google.code.gson:gson:${Versions.gson}"

    // okHttp
    const val okHttp = "com.squareup.okhttp3:okhttp:${Versions.okHttp}"

    // Retrofit
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val gsonConverter = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"

    // Coroutines
    const val coroutineCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutine}"

    // Store
    const val store = "com.dropbox.mobile.store:store4:${Versions.store}"

    // Dagger
    const val dagger = "com.google.dagger:dagger:${Versions.dagger}"
    const val daggerAndroid = "com.google.dagger:dagger-android:${Versions.dagger}"
    const val daggerAndroidSupport = "com.google.dagger:dagger-android-support:${Versions.dagger}"
    const val daggerCompiler = "com.google.dagger:dagger-compiler:${Versions.dagger}"
    const val daggerProcessor = "com.google.dagger:dagger-android-processor:${Versions.dagger}"

    // Fragment
    const val fragment = "androidx.fragment:fragment:${Versions.fragment}"
    const val fragmentKtx = "androidx.fragment:fragment-ktx:${Versions.fragment}"
}