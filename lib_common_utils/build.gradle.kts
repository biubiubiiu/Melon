plugins {
    id(BuildPlugins.kotlin)

    id(ScriptPlugins.infrastructure)
}

dependencies {
    implementation(Libraries.kotlinStdLib)

    api(Libraries.Retrofit.retrofit)
    api(Libraries.Coroutine.core)
}