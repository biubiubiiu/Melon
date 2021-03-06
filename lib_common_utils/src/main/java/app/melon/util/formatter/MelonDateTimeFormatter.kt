package app.melon.util.formatter

import android.text.format.DateUtils
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

    fun formatShortDate(isoTimeString: String?): String {
        isoTimeString ?: return ""
        return formatShortDate(isoTimeString.toOffsetDateTime())
    }

    fun formatMediumDate(isoTimeString: String?): String {
        isoTimeString ?: return ""
        return formatMediumDate(isoTimeString.toOffsetDateTime())
    }

    fun formatMediumDateTime(isoTimeString: String?): String {
        isoTimeString ?: return ""
        return formatMediumDateTime(isoTimeString.toOffsetDateTime())
    }

    private fun formatShortDate(temporalAmount: Temporal) =
        exceptionWrapper { shortDateFormatter.format(temporalAmount) }

    private fun formatMediumDate(temporalAmount: Temporal) =
        exceptionWrapper { mediumDateFormatter.format(temporalAmount) }

    private fun formatMediumDateTime(temporalAmount: Temporal) =
        exceptionWrapper { mediumDateTimeFormatter.format(temporalAmount) }

    fun formatShortTime(localTime: LocalTime) = exceptionWrapper { shortTimeFormatter.format(localTime) }

    private fun exceptionWrapper(block: () -> String): String {
        return try {
            block()
        } catch (e: Exception) {
            "ERROR"
        }
    }

    fun formatShortRelativeTime(isoTimeString: String?) = exceptionWrapper {
        isoTimeString ?: return@exceptionWrapper ""
        formatShortRelativeTime(isoTimeString.toOffsetDateTime())
    }

    private fun formatShortRelativeTime(dateTime: OffsetDateTime): String {
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

    // TODO use official library
    fun formatDuration(second: Long): String {
        if (second > 3600) {
            val hour = second / 3600
            val minute = second % 3600 / 60
            return hour.toString() + "??????" + minute + "??????"
        }
        if (second >= 60) {
            val minute = second / 60
            return minute.toString() + "??????"
        }
        return second.toString() + "???"
    }

    private inline fun String.toOffsetDateTime(
        formatter: DateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
    ): OffsetDateTime {
        return formatter.parse(this, OffsetDateTime::from)
    }
}