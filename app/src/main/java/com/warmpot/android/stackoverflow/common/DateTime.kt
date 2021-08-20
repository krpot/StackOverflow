package com.warmpot.android.stackoverflow.common

import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@JvmInline
value class EpochSecond(
    val time: Long // 10 digits
)

enum class DateTimeFormatType(val pattern: String) {
    ddMMyyyy(pattern = "dd-MM-yyyy"),
    ddMMyy(pattern = "dd-MM-yy")
}

fun EpochSecond.format(formatType: DateTimeFormatType = DateTimeFormatType.ddMMyyyy): String {
    val instant = Instant.ofEpochSecond(this.time)
    val ofInstant = ZonedDateTime.ofInstant(instant, ZoneOffset.UTC)
    return ofInstant.format(DateTimeFormatter.ofPattern(formatType.pattern))
}
