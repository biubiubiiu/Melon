package app.melon.base.ui.imageloading

import app.melon.base.ui.R
import coil.annotation.ExperimentalCoilApi
import coil.intercept.Interceptor
import coil.request.ImageResult


@OptIn(ExperimentalCoilApi::class)
class CoilInterceptor : Interceptor {

    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
        val request = when (val data = chain.request.data) {
            (data as? String)?.startsWith("local-resource:") == true -> {
                chain.request.newBuilder()
                    .data(map(data as String))
                    .build()
            }
            else -> chain.request
        }
        return chain.proceed(request)
    }

    private fun map(data: String): Any {
        return when (data.removePrefix("local-resource:")) {
            "avatar_anonymous" -> R.drawable.ic_avatar_anonymous
            else -> data
        }
    }
}