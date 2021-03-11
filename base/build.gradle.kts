plugins {
    id(BuildPlugins.kotlin)
}

dependencies {
    api(Libraries.kotlinStdLib)
    api(Libraries.coroutineCore)
    api(Libraries.retrofit)
    api(Libraries.dagger)
    api(Libraries.daggerAndroid)
}