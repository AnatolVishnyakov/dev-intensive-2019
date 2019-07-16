package ru.skillbranch.devintensive.extensions

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

const val SECOND = 1000L
const val MINUTE = 60 * SECOND
const val HOUR = 60 * MINUTE
const val DAY = 24 * HOUR
const val YEAR = 366 * DAY

private val DECLENSION_PERIOD = object : HashMap<String, String>() {
    init {
        // секунды
        put("SECOND_1", "секунду")
        put("SECOND_2", "секунды")
        put("SECOND_5", "секунд")
        // минуты
        put("MINUTE_1", "минуту")
        put("MINUTE_2", "минуты")
        put("MINUTE_5", "минут")
        // часы
        put("HOUR_1", "час")
        put("HOUR_2", "часа")
        put("HOUR_5", "часов")
        // дни
        put("DAY_1", "день")
        put("DAY_2", "дня")
        put("DAY_5", "дней")
        // годы
        put("YEAR_1", "год")
        put("YEAR_2", "года")
        put("YEAR_5", "лет")
    }
}

private fun plurals(n: Long): Long {
    var value = n
    if (value == 0L) {
        return 0
    } else {
        value = abs(value) % 100
        val lastSymbol = value % 10
        if (value in 11..19) {
            return 5
        }
        if (lastSymbol in 2..4) {
            return 2
        }
        if (lastSymbol == 1L) {
            return 1
        }
        return 5
    }
}

private fun declension(value: Long, units: TimeUnits): String? {
    return when (plurals(value)) {
        1L -> DECLENSION_PERIOD["${units.name}_1"]
        in 2..4 -> DECLENSION_PERIOD["${units.name}_2"]
        else -> DECLENSION_PERIOD["${units.name}_5"]
    }
}

fun Date.format(pattern: String = "HH:mm:ss dd.MM.yy"): String {
    return SimpleDateFormat(pattern, Locale("ru")).format(this)
}

fun Date.add(value: Int, units: TimeUnits = TimeUnits.SECOND): Date {
    this.time += when (units) {
        TimeUnits.SECOND -> value * SECOND
        TimeUnits.MINUTE -> value * MINUTE
        TimeUnits.HOUR -> value * HOUR
        TimeUnits.DAY -> value * DAY
        TimeUnits.YEAR -> value * YEAR
    }

    return this
}

fun Date.trimToMilliSecond(): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar.time
}

fun Date.humanizeDiff(date: Date = Date()): String {
    val prefix = if (this > date) "через " else ""
    val postfix = if (this < date) " назад" else ""

    val currentDate = this.trimToMilliSecond()
    val otherDate = date.trimToMilliSecond()
    val diffTime = if (otherDate > currentDate) otherDate.time - currentDate.time
    else currentDate.time - otherDate.time

    return when (diffTime) {
        in 0..SECOND -> "только что"
        in SECOND..45 * SECOND -> "${prefix}несколько секунд$postfix"
        in 45 * SECOND..75 * SECOND -> "${prefix}минуту$postfix"
        in 75 * SECOND..45 * MINUTE -> "$prefix${diffTime / MINUTE} ${declension(
            diffTime / MINUTE,
            TimeUnits.MINUTE
        )}$postfix"
        in 45 * MINUTE..75 * MINUTE -> "${prefix}час$postfix"
        in 75 * MINUTE..22 * HOUR -> "$prefix${diffTime / HOUR} ${declension(diffTime / HOUR, TimeUnits.HOUR)}$postfix"
        in 22 * HOUR..26 * HOUR -> "${prefix}день$postfix"
        in 26 * HOUR..360 * DAY -> "$prefix${diffTime / DAY} ${declension(diffTime / DAY, TimeUnits.DAY)}$postfix"
        else -> if (this.time < date.time) "более года назад" else "более чем через год"
    }
}

enum class TimeUnits {
    SECOND {
        override fun plural(value: Long): String? {
            return "$value ${declension(value, SECOND)}"
        }
    },
    MINUTE {
        override fun plural(value: Long): String? {
            return "$value ${declension(value, MINUTE)}"
        }
    },
    HOUR {
        override fun plural(value: Long): String? {
            return "$value ${declension(value, HOUR)}"
        }
    },
    DAY {
        override fun plural(value: Long): String? {
            return "$value ${declension(value, DAY)}"
        }
    },
    YEAR {
        override fun plural(value: Long): String? {
            return "$value ${declension(value, YEAR)}"
        }
    };

    abstract fun plural(value: Long): String?
}