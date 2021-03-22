plugins {
    id(BuildPlugins.kotlin)
    id(BuildPlugins.kotlinKapt)
}

kapt {
    correctErrorTypes = true
    useBuildCache = true
}

dependencies {
    api(project(Modules.base))

    api(Libraries.Room.common)
    api(Libraries.Gson.gson)
    api(Libraries.Retrofit.gsonConverter)
    api(Libraries.Retrofit.retrofit)
    api(Libraries.Paging3.common)
    api(Libraries.Store.store)

    kapt(Libraries.Dagger.compiler)
}