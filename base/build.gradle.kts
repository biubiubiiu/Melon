plugins {
    id(BuildPlugins.kotlin)
}

dependencies {
    api(Libraries.kotlinStdLib)
    api(Libraries.Coroutine.core)
    api(Libraries.Retrofit.retrofit)
    api(Libraries.Dagger.dagger)
    api(Libraries.Dagger.android)
}