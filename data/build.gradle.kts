plugins {
    id(BuildPlugins.kotlin)
    id(BuildPlugins.kotlinKapt)
}

kapt {
    correctErrorTypes = true
    useBuildCache = true
}

dependencies {
    api(Libraries.Room.common)
    api(Libraries.Gson.gson)
    api(Libraries.Retrofit.gsonConverter)
    api(Libraries.Retrofit.retrofit)
    api(Libraries.Paging3.common)
    api(Libraries.Dagger.dagger)

    kapt(Libraries.Dagger.compiler)
}