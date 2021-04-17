package app.melon.util.formatter

import android.text.format.DateUtils
import app.melon.util.extensions.toOffsetDateTime
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.Temporal
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Taken from: https://github.com/chrisbanes/tivi
 */
@Singleton
class MelonDateTimeFormatter @Inject constructor(
    @ShortTime private val shortTimeFormatter: DateTimeFormatter,
    @ShortDate private val shortDateFormatter: DateTimeFormatter,
    @MediumDate private val mediumDateFormatter: DateTimeFormatter,
    @MediumDateTime private val mediumDateTimeFormatter: DateTimeFormatter
) {

    fun formatShortDate(isoTimeString: String) = formatShortDate(isoTimeString.toOffsetDateTime())
    fun formatMediumDate(isoTimeString: String) = formatMediumDate(isoTimeString.toOffsetDateTime())
    fun formatMediumDateTime(isoTimeString: String) = formatMediumDateTime(isoTimeString.toOffsetDateTime())

    fun formatShortDate(temporalAmount: Temporal) = exceptionWrapper { shortDateFormatter.format(temporalAmount) }
    fun formatMediumDate(temporalAmount: Temporal) = exceptionWrapper { mediumDateFormatter.format(temporalAmount) }
    fun formatMediumDateTime(temporalAmount: Temporal) = exceptionWrapper { mediumDateTimeFormatter.format(temporalAmount) }
    fun formatShortTime(localTime: LocalTime) = exceptionWrapper { shortTimeFormatter.format(localTime) }

    private fun exceptionWrapper(block: () -> String): String {
        return try {
            block()
        } catch (e: Exception) {
            "ERROR"
        }
    }

    fun formatShortRelativeTime(isoTimeString: String) = exceptionWrapper {
        formatShortRelativeTime(isoTimeString.toOffsetDateTime())
    }

    fun formatShortRelativeTime(dateTime: OffsetDateTime): String {
        val now = OffsetDateTime.now()

        return if (dateTime.isBefore(now)) {
            if (dateTime.year == now.year || dateTime.isAfter(now.minusDays(7))) {
                // Within the past week
                DateUtils.getRelativeTimeSpanString(
                    dateTime.toInstant().toEpochMilli(),
                    System.currentTimeMillis(),
                    DateUtils.MINUTE_IN_MILLIS,
                    DateUtils.FORMAT_SHOW_DATE
                ).toString()
            } else {
                // More than 7 days ago
                formatShortDate(dateTime)
            }
        } else {
            if (dateTime.year == now.year || dateTime.isBefore(now.plusDays(14))) {
                // In the near future (next 2 weeks)
                DateUtils.getRelativeTimeSpanString(
                    dateTime.toInstant().toEpochMilli(),
                    System.currentTimeMillis(),
                    DateUtils.MINUTE_IN_MILLIS,
                    DateUtils.FORMAT_SHOW_DATE
                ).toString()
            } else {
                // In the far future
                formatShortDate(dateTime)
            }
        }
    }
}