package app.melon.framework.helper

import android.app.Application
import androidx.core.os.ConfigurationCompat
import app.melon.base.initializer.AppInitializer
import okhttp3.Interceptor
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
internal class LocaleManager @Inject constructor() : AppInitializer {

    private var localeParam: String? = null

    private fun updateLocale(locale: Locale) {
        this.localeParam = when (locale) {
            Locale.CHINESE, Locale.CHINA -> "zh"
            else -> "en"
        }
    }

    internal fun provideInterceptor() = Interceptor { chain ->
        val original = chain.request()
        if (localeParam == null) {
            chain.proceed(original)
        } else {
            val url = original.url.newBuilder().addQueryParameter(LOCALE_PARAM_NAME, localeParam).build()
            val request = original.newBuilder().url(url).build()
            chain.proceed(request)
        }
    }

    companion object {
        private const val LOCALE_PARAM_NAME = "locale"
    }

    override fun init(application: Application) {
        val locales = ConfigurationCompat.getLocales(application.resources.configuration)
        val locale = if (locales.isEmpty) {
            Locale.CHINA
        } else {
            locales.get(0)
        }
        updateLocale(locale)
    }
}