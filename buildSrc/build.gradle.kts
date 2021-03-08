object Dependencies {
    const val AndroidBuildTools = "com.android.tools.build:gradle:4.1.2"
    const val KotlinBuildTools = "org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.72"
}

plugins {
    `kotlin-dsl`
}

repositories {
    jcenter()
    google()
}

dependencies {
    implementation(Dependencies.AndroidBuildTools)
    implementation(Dependencies.KotlinBuildTools)
}