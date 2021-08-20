package com.warmpot.android.stackoverflow.common

import java.time.Duration
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

class Ago(
    val mills: Long
) {
    private val seconds = mills / 1000
    private val minutes = seconds / 60
    private val hours = minutes / 60
    private val days = hours / 24

    private val daysText: String get() = days.toUnitText("day")
    private val hoursText: String get() = hours.toUnitText("hour")
    private val minutesText: String get() = minutes.toUnitText("min")
    private val secondsText: String get() = seconds.toUnitText("sec")

    val text: String
        get() {
            if (days > 0) return "$daysText ago"
            if (hours > 0) return "$hoursText ago"
            if (minutes > 0) return "$minutesText ago"
            if (seconds > 0) return "$secondsText ago"
            return "just now"
        }
}

private fun Long.toUnitText(unit: String): String {
    return when (this) {
        0L -> ""
        1L -> "$this $unit"
        else -> "$this ${unit}s"
    }
}

fun EpochSecond.agoText(): String {
    val instant1 = Instant.ofEpochSecond(System.currentTimeMillis() / 1000L)
    val instant2 = Instant.ofEpochSecond(this.time)

    return try {
        val diff = Duration.between(instant2, instant1).toMillis()
        Ago(diff).text
    } catch (e: RuntimeException) {
        ""
    }
}
