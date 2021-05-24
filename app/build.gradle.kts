import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    // Application Specific Plugins
    id(BuildPlugins.androidApplication)
    id(BuildPlugins.kotlinAndroid)
    id(BuildPlugins.kotlinAndroidExtensions)
    id(BuildPlugins.kotlinKapt)

    // Firebase
    id(FirebaseService.crashlytics)

    // AndResGuard
    id(BuildPlugins.AndResGuard.AndResGuardPlugin)

    // ByteX
    id(BuildPlugins.ByteX.byteXPlugin)
    id(BuildPlugins.ByteX.byteXConstInlinePlugin)

    // Internal Script plugins
    id(ScriptPlugins.compilation)
    id(ScriptPlugins.infrastructure)
}

android {
    compileSdkVersion(AndroidSDK.compile)
    buildToolsVersion(AndroidSDK.buildToolsVersion)

    ndkVersion = AndroidSDK.ndkVersion

    signingConfigs {
        getByName("debug") {
            storeFile = file(gradleLocalProperties(rootDir).getProperty("keystore.debug.filename"))
            storePassword = gradleLocalProperties(rootDir).getProperty("keystore.debug.storePassword")
            keyAlias = gradleLocalProperties(rootDir).getProperty("keystore.debug.keyAlias")
            keyPassword = gradleLocalProperties(rootDir).getProperty("keystore.debug.keyPassword")
        }
        create("release") {
            storeFile = file(gradleLocalProperties(rootDir).getProperty("keystore.release.filename"))
            storePassword = gradleLocalProperties(rootDir).getProperty("keystore.release.storePassword")
            keyAlias = gradleLocalProperties(rootDir).getProperty("keystore.release.keyAlias")
            keyPassword = gradleLocalProperties(rootDir).getProperty("keystore.release.keyPassword")
        }
    }

    defaultConfig {
        minSdkVersion(AndroidSDK.min)
        targetSdkVersion(AndroidSDK.target)
        multiDexEnabled = true

        applicationId = AndroidClient.appId
        versionCode = AndroidClient.versionCode
        versionName = AndroidClient.versionName

        vectorDrawables {
            useSupportLibrary = true
        }

        manifestPlaceholders["amap_api_key"] = gradleLocalProperties(rootDir).getProperty("amap_api_key")
        manifestPlaceholders["JPUSH_PKGNAME"] = AndroidClient.appId
        manifestPlaceholders["JPUSH_APPKEY"] = gradleLocalProperties(rootDir).getProperty("jpush_api_key")
        manifestPlaceholders["JPUSH_CHANNEL"] = "developer-default"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("release")
            proguardFiles("proguard-rules.pro")
        }
        getByName("debug") {
            signingConfig = signingConfigs.getByName("debug")
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
    implementation(project(Modules.baseFramework))
    implementation(project(Modules.commonAnimation))
    implementation(project(Modules.dataAndroid))
    implementation(project(Modules.user))
    implementation(project(Modules.account))
    implementation(project(Modules.event))
    implementation(project(Modules.feed))
    implementation(project(Modules.im))
    implementation(project(Modules.comment))
    implementation(project(Modules.group))
    implementation(project(Modules.poi))
    implementation(project(Modules.gallery))
    implementation(project(Modules.composer))
    implementation(project(Modules.settings))
    implementation(project(Modules.framework))
    implementation(project(Modules.commonAnimation))

    implementation(project(Modules.accountApi))
    implementation(project(Modules.userApi))
    implementation(project(Modules.eventApi))
    implementation(project(Modules.poiApi))
    implementation(project(Modules.composerApi))

    implementation(Libraries.MultiDex.multiDex)

    implementation(Libraries.AndroidX.Core.ktx)

    implementation(platform(Libraries.Firebase.bom))
    implementation(Libraries.Firebase.analytics)
    implementation(Libraries.Firebase.crashlytics)

    implementation(Libraries.Cropping.uCrop)

    kapt(Libraries.Dagger.compiler)
    kapt(Libraries.Dagger.processor)
}

andResGuard {
    mappingFile = null
    use7zip = true
    useSign = true
    keepRoot = false
    fixedResName = "arg"
    mergeDuplicatedRes = true
    whiteList = listOf(
        "R.drawable.jpush_notification_icon",
        "R.bool.com.crashlytics.useFirebaseAppId",
        "R.string.com.crashlytics.useFirebaseAppId",
        "R.string.google_app_id",
        "R.bool.com.crashlytics.CollectDeviceIdentifiers",
        "R.string.com.crashlytics.CollectDeviceIdentifiers",
        "R.bool.com.crashlytics.CollectUserIdentifiers",
        "R.string.com.crashlytics.CollectUserIdentifiers",
        "R.string.com.crashlytics.ApiEndpoint",
        "R.string.io.fabric.android.build_id",
        "R.string.com.crashlytics.android.build_id",
        "R.bool.com.crashlytics.RequireBuildId",
        "R.string.com.crashlytics.RequireBuildId",
        "R.bool.com.crashlytics.CollectCustomLogs",
        "R.string.com.crashlytics.CollectCustomLogs",
        "R.bool.com.crashlytics.Trace",
        "R.string.com.crashlytics.Trace",
        "R.string.com.crashlytics.CollectCustomKeys"
    )
    compressFilePattern = arrayListOf(
        "*.png",
        "*.jpg",
        "*.jpeg"
    )
    sevenzip {
        artifact = "com.tencent.mm:SevenZip:1.2.21"
    }

    digestalg = "SHA-256"
}

ByteX {
    enable(true)
    enableInDebug(false)
    logLevel("DEBUG")
}

const_inline {
    enable(true)
    enableInDebug(true)
    logLevel("INFO")
    isAutoFilterReflectionField = true
    isSkipWithRuntimeAnnotation = true
    skipWithAnnotations = hashSetOf(
        "android/support/annotation/Keep"
    )
}

apply(plugin = BuildPlugins.benchmarkPlugin)
apply(plugin = FirebaseService.googleService)