package app.melon.util.extensions

import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

inline fun String.toOffsetDateTime(formatter: DateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME): OffsetDateTime {
    return formatter.parse(this, OffsetDateTime::from)
}

