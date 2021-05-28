package app.melon.util.di

import android.content.Context
import androidx.core.os.ConfigurationCompat
import app.melon.util.formatter.MediumDate
import app.melon.util.formatter.MediumDateTime
import app.melon.util.formatter.ShortDate
import app.melon.util.formatter.ShortTime
import app.melon.util.timesync.TimeService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import javax.inject.Singleton


@Module(
    includes = [
        DateTimeFormatterModule::class,
        TimeRemoteServiceModule::class,
        StorageModule::class,
        NetworkModule::class
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

@Module
class TimeRemoteServiceModule {
    @Provides
    fun provideTimeService(retrofit: Retrofit): TimeService = retrofit.create(TimeService::class.java)
}