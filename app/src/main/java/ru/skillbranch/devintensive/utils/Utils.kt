package ru.skillbranch.devintensive.utils

import ru.skillbranch.devintensive.extensions.transliterate

object Utils {
    fun parseFullName(fullName: String?): Pair<String?, String?> {
        val parts: List<String>? = fullName?.split(" ")
        val firstName = parts?.getOrNull(0)
        val lastName = parts?.getOrNull(1)

        // эквивалент Pair(firstName, lastName)
        return firstName to lastName
    }

    fun transliteration(payload: String, divider: String = " "): String {
        val sb = StringBuilder()
        val records = payload.split(" ")
        for (record in records) {
            sb.append("${record.transliterate()}$divider")
        }
        return sb.toString()
    }

    fun toInitials(firstName: String?, lastName: String?): String? {
        return "${firstName?.get(0)}${lastName?.get(0)}"
    }
}