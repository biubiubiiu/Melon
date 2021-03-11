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

    api(Libraries.kotlinStdLib)
    api(Libraries.roomCommon)
    api(Libraries.gson)
    api(Libraries.gsonConverter)
    api(Libraries.paging3Common)
    api(Libraries.retrofit)
    api(Libraries.store)

    kapt(Libraries.daggerCompiler)
}