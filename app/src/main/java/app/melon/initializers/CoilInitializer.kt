package app.melon.initializers

import android.app.Application
import android.content.Context
import app.melon.base.initializer.AppInitializer
import app.melon.base.ui.imageloading.CoilInterceptor
import coil.Coil
import coil.ImageLoader
import coil.util.CoilUtils
import okhttp3.OkHttpClient
import javax.inject.Inject


class CoilInitializer @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val context: Context
) : AppInitializer {

    private val interceptor = CoilInterceptor()

    override fun init(application: Application) {
        val coilOkHttpClient = okHttpClient.newBuilder()
            .cache(CoilUtils.createDefaultCache(context))
            .build()
        Coil.setImageLoader(
            ImageLoader.Builder(application)
                .componentRegistry {
                    add(interceptor)
                }
                .okHttpClient(coilOkHttpClient)
                .build()
        )
    }
}