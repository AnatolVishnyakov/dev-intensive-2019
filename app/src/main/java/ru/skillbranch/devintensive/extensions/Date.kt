package ru.skillbranch.devintensive.extensions

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs


const val SECOND = 1000L
const val MINUTE = 60 * SECOND
const val HOUR = 60 * MINUTE
const val DAY = 24 * HOUR
const val YEAR = 366 * DAY

fun Date.format(pattern: String = "HH:mm:ss dd.MM.yy"): String {
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

fun Date.add(value: Int, units: TimeUnits = TimeUnits.SECOND): Date {
    var time = this.time

    time += when (units) {
        TimeUnits.SECOND -> value * SECOND
        TimeUnits.MINUTE -> value * MINUTE
        TimeUnits.HOUR -> value * HOUR
        TimeUnits.DAY -> value * DAY
        TimeUnits.YEAR -> value * YEAR
    }

    this.time = time
    return this
}

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
    var value = plurals(value)
    return when (value) {
        1L -> DECLENSION_PERIOD["${units.name}_1"]
        in 2..4 -> DECLENSION_PERIOD["${units.name}_2"]
        else -> DECLENSION_PERIOD["${units.name}_5"]
    }
}

fun Date.humanizeDiff(date: Date = Date()): String {
    var prefix = if (this > date) "через " else ""
    var postfix = if (this < date) " назад" else ""
    val diffTime = if (date > this) date.time - this.time else this.time - date.time
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
    SECOND,
    MINUTE,
    HOUR,
    DAY,
    YEAR
}