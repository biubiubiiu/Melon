package app.melon.util.di

import android.content.Context
import androidx.core.os.ConfigurationCompat
import app.melon.util.time.MediumDate
import app.melon.util.time.MediumDateTime
import app.melon.util.time.ShortDate
import app.melon.util.time.ShortTime
import dagger.Module
import dagger.Provides
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import javax.inject.Singleton


@Module(
    includes = [
        DateTimeFormatterModule::class
    ]
)
class UtilModule


@Module
class DateTimeFormatterModule {

    private fun DateTimeFormatter.withLocale(context: Context): DateTimeFormatter {
        val locales = ConfigurationCompat.getLocales(context.resources.configuration)
        return when {
            locales.isEmpty -> this
            else -> withLocale(locales[0])
        }
    }

    @Provides
    @ShortTime
    fun provideShortTimeFormatter(context: Context): DateTimeFormatter {
        return DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(context)
    }

    @Provides
    @ShortDate
    fun provideShortDateFormatter(context: Context): DateTimeFormatter {
        return DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(context)
    }

    @Singleton
    @Provides
    @MediumDate
    fun provideMediumDateFormatter(context: Context): DateTimeFormatter {
        return DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(context)
    }

    @Singleton
    @Provides
    @MediumDateTime
    fun provideDateTimeFormatter(context: Context): DateTimeFormatter {
        return DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(context)
    }
}