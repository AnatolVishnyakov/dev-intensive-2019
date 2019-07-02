package ru.skillbranch.devintensive.utils

import ru.skillbranch.devintensive.extensions.transliterate

object Utils {
    fun parseFullName(fullName: String?): Pair<String?, String?> {
        return if (fullName.isNullOrBlank()) {
            null to null
        } else {
            val parts: List<String>? = fullName.trim().split(" ")
            val firstName = parts?.getOrNull(0)
            val lastName = parts?.getOrNull(1)

            // эквивалент Pair(firstName, lastName)
            firstName to lastName
        }
    }

    fun transliteration(payload: String, divider: String = " "): String {
        return if (payload != " ") {
            val sb = StringBuilder()
            val records = payload
                .split(" ")

            for (record in records) {
                if (sb.isNotEmpty()) {
                    sb.append(divider)
                }
                sb.append(record.transliterate())
            }
            sb.toString()
        } else {
            " "
        }
    }

    fun toInitials(firstName: String?, lastName: String?): String? {
        return if (firstName.isNullOrBlank() && lastName.isNullOrBlank()) {
            null
        } else {
            (if (!firstName.isNullOrBlank()) "${firstName[0].toUpperCase()}" else "") +
                    if (!lastName.isNullOrBlank()) "${lastName[0].toUpperCase()}" else ""
        }
    }
}