object Kotlin {
    const val standardLibrary = "1.3.72"
}

object AndroidSDK {
    const val min = 19
    const val compile = 30
    const val target = compile
    const val buildToolsVersion = "30.0.3"
    const val ndkVersion = "23.0.7272597"
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
        const val benchmarkVersion = "1.0.0"
    }

    const val androidGradlePlugin = "com.android.tools.build:gradle:${Versions.buildToolsVersion}"
    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Kotlin.standardLibrary}"
    const val androidApplication = "com.android.application"
    const val androidLibrary = "com.android.library"
    const val kotlin = "kotlin"
    const val kotlinAndroid = "kotlin-android"
    const val kotlinKapt = "kotlin-kapt"
    const val kotlinAndroidExtensions = "kotlin-android-extensions"
    const val benchmark = "androidx.benchmark:benchmark-gradle-plugin:${Versions.benchmarkVersion}"
    const val benchmarkPlugin = "androidx.benchmark"

    object AndResGuard {
        private const val version  ="1.2.21"
        const val AndResGuard = "com.tencent.mm:AndResGuard-gradle-plugin:$version"
        const val AndResGuardPlugin = "AndResGuard"
    }

    object ByteX {
        private const val version = "0.2.7"
        const val byteX = "com.bytedance.android.byteX:base-plugin:$version"
        const val byteX_const_inline = "com.bytedance.android.byteX:const-inline-plugin:$version"
        const val byteXPlugin = "bytex"
        const val byteXConstInlinePlugin = "bytex.const_inline"
    }
}

object FirebaseService {
    const val googleServicePlugin = "com.google.gms:google-services:4.3.5"
    const val crashlyticsPlugin = "com.google.firebase:firebase-crashlytics-gradle:2.5.2"

    const val googleService = "com.google.gms.google-services"
    const val crashlytics = "com.google.firebase.crashlytics"
}

object ScriptPlugins {
    const val infrastructure = "scripts.infrastructure"
    const val compilation = "scripts.compilation"
}

object Modules {
    const val baseFramework = ":lib_base_framework"
    const val data = ":data"
    const val dataAndroid = ":data-android"

    const val user = ":module_user"
    const val userApi = ":module_user_api"

    const val commonUi = ":lib_common_ui"
    const val commonAnimation = ":lib_common_animation"
    const val commonUtils = ":lib_common_utils"
    const val runtimePermissions = ":lib_permission"
    const val location = ":lib_location"


    const val im = ":module_im"
    const val feed = ":module_feed"
    const val group = ":module_group"
    const val comment = ":module_comment"
    const val gallery = ":module_gallery"
    const val settings = ":module_settings"
    const val framework = ":module_framework"

    const val homeApi = ":module_home_api"

    const val composerApi = ":module_composer_api"
    const val composer = ":module_composer"

    const val poiApi = ":module_poi_api"
    const val poi = ":module_poi"

    const val accountApi = ":module_account_api"
    const val account = ":module_account"

    const val eventApi = ":module_event_api"
    const val event = ":module_event"
}

object Libraries {

    const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Kotlin.standardLibrary}"

    object JavaSDK {
        object Desugar {
            private const val version = "1.0.9"
            const val desugar = "com.android.tools:desugar_jdk_libs:$version"
        }
    }

    object AndroidX {
        object Core {
            private const val version = "1.5.0-rc01"
            const val ktx = "androidx.core:core-ktx:$version"
        }

        object AppCompat {
            private const val version = "1.3.0-alpha02"
            const val appCompat = "androidx.appcompat:appcompat:$version"
        }

        object Activity {
            private const val version = "1.2.0-alpha08"
            const val activity = "androidx.activity:activity:$version"
            const val ktx = "androidx.activity:activity-ktx:$version"
        }

        object Fragment {
            private const val version = "1.3.0-alpha08"
            const val fragment = "androidx.fragment:fragment:$version"
            const val ktx = "androidx.fragment:fragment-ktx:$version"
        }

        object Lifecycle {
            private const val version = "2.2.0"
            const val common = "androidx.lifecycle:lifecycle-common-java8:$version"
        }
    }

    object RecyclerView {
        private const val version = "1.1.0"
        const val recyclerView = "androidx.recyclerview:recyclerview:$version"
    }

    object SwipeRefreshLayout {
        private const val version = "1.1.0"
        const val swipeRefreshLayout = "androidx.swiperefreshlayout:swiperefreshlayout:$version"
    }

    object ConstraintLayout {
        private const val version = "2.0.4"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:$version"
    }

    object Material {
        private const val version = "1.3.0"
        const val material = "com.google.android.material:material:$version"
    }

    object MultiDex {
        private const val version = "2.0.1"
        const val multiDex = "androidx.multidex:multidex:$version"
    }

    object Navigation {
        private const val version = "2.2.2"
        const val fragmentKtx = "androidx.navigation:navigation-fragment-ktx:$version"
        const val uiKtx = "androidx.navigation:navigation-ui-ktx:$version"
    }

    object Transition {
        private const val version = "1.4.0"
        const val transition = "androidx.transition:transition:$version"
        const val ktx = "androidx.transition:transition-ktx:$version"
    }

    object DynamicAnimation {
        private const val version = "1.0.0-alpha03"
        const val animation ="androidx.dynamicanimation:dynamicanimation-ktx:$version"
    }

    object Coil {
        private const val version = "1.1.1"
        const val coil = "io.coil-kt:coil:$version"
    }

    object Epoxy {
        private const val version = "4.4.2"
        const val runtime = "com.airbnb.android:epoxy:$version"
        const val compiler = "com.airbnb.android:epoxy-processor:$version"
        const val paging = "com.airbnb.android:epoxy-paging3:$version"
    }

    object Lottie {
        private const val version = "3.7.0"
        const val lottie = "com.airbnb.android:lottie:$version"
    }

    object Paging3 {
        private const val version = "3.0.0-alpha13"
        const val common = "androidx.paging:paging-common:$version"
        const val runtime = "androidx.paging:paging-runtime:$version"
    }

    object Room {
        private const val version = "2.3.0-alpha04"
        const val common = "androidx.room:room-common:$version"
        const val runtime = "androidx.room:room-runtime:$version"
        const val compiler = "androidx.room:room-compiler:$version"
        const val ktx = "androidx.room:room-ktx:$version"
    }

    object Gson {
        private const val version = "2.8.6"
        const val gson = "com.google.code.gson:gson:$version"
    }

    object OkHttp {
        private const val version = "4.9.0"
        const val okHttp = "com.squareup.okhttp3:okhttp:$version"
    }

    object Retrofit {
        private const val version = "2.9.0"
        const val retrofit = "com.squareup.retrofit2:retrofit:$version"
        const val gsonConverter = "com.squareup.retrofit2:converter-gson:$version"
    }

    object Coroutine {
        private const val version = "1.3.9"
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
    }

    object Dagger {
        private const val version = "2.33"
        const val dagger = "com.google.dagger:dagger:$version"
        const val android = "com.google.dagger:dagger-android:$version"
        const val androidSupport = "com.google.dagger:dagger-android-support:$version"
        const val compiler = "com.google.dagger:dagger-compiler:$version"
        const val processor = "com.google.dagger:dagger-android-processor:$version"
    }

    object PhotoView {
        private const val version = "2.3.0"
        const val photoView = "com.github.chrisbanes:PhotoView:$version"
    }

    object Shimmer {
        private const val version = "0.5.0"
        const val shimmer = "com.facebook.shimmer:shimmer:$version"
    }

    object Cropping {
        private const val version = "2.2.6"
        const val uCrop = "com.github.yalantis:ucrop:$version"
    }

    object Firebase {
        private const val bomVersion = "27.1.0"
        const val bom = "com.google.firebase:firebase-bom:$bomVersion"
        const val analytics = "com.google.firebase:firebase-analytics-ktx"
        const val crashlytics = "com.google.firebase:firebase-crashlytics-ktx"
    }

    object Preference {
        private const val version = "1.1.0"
        const val preference = "androidx.preference:preference-ktx:$version"
    }
}