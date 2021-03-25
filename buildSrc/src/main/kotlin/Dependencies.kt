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
    const val androidLibrary = "com.android.library"
    const val kotlin = "kotlin"
    const val kotlinAndroid = "kotlin-android"
    const val kotlinKapt = "kotlin-kapt"
    const val kotlinAndroidExtensions = "kotlin-android-extensions"
    const val WMRouter = "WMRouter"
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
    const val user = ":module_user"
    const val commonUi = ":lib_common_ui"
    const val feed = ":module_feed"
}

object Libraries {

    const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Kotlin.standardLibrary}"

    object AndroidX {
        object Core {
            private const val version = "1.2.0"
            const val ktx = "androidx.core:core-ktx:$version"
        }

        object AppCompat {
            private const val version = "1.2.0"
            const val appCompat = "androidx.appcompat:appcompat:$version"
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
        const val fragment = "androidx.navigation:navigation-fragment:$version"
        const val ui = "androidx.navigation:navigation-ui:$version"
        const val fragmentKtx = "androidx.navigation:navigation-fragment-ktx:$version"
        const val uiKtx = "androidx.navigation:navigation-ui-ktx:$version"
    }

    object Lifecycle {
        private const val version = "2.3.0"
        const val liveDataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:$version"
        const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"

    }

    object Router {
        private const val version = "1.2.0"
        const val runtime = "com.sankuai.waimai.router:router:$version"
        const val compiler = "com.sankuai.waimai.router:compiler:$version"
        const val plugin = "com.sankuai.waimai.router:plugin:$version"
    }

    object Transition {
        private const val version = "1.4.0"
        const val transition = "androidx.transition:transition:$version"
        const val ktx = "androidx.transition:transition-ktx:$version"
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

    object Mavericks {
        private const val version = "2.1.0"
        const val mavericks = "com.airbnb.android:mavericks:$version"
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

    object Store {
        private const val version = "4.0.0"
        const val store = "com.dropbox.mobile.store:store4:$version"
    }

    object Dagger {
        private const val version = "2.33"
        const val dagger = "com.google.dagger:dagger:$version"
        const val android = "com.google.dagger:dagger-android:$version"
        const val androidSupport = "com.google.dagger:dagger-android-support:$version"
        const val compiler = "com.google.dagger:dagger-compiler:$version"
        const val processor = "com.google.dagger:dagger-android-processor:$version"
    }

    object Fragment {
        private const val version = "1.3.0-beta02"
        const val fragment = "androidx.fragment:fragment:$version"
        const val ktx = "androidx.fragment:fragment-ktx:$version"
    }
}