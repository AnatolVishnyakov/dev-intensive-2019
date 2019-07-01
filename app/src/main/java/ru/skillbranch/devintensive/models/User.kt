package ru.skillbranch.devintensive.models

import ru.skillbranch.devintensive.utils.Utils
import java.util.*

// Первичный конструктор
data class User(
    val id: String,
    var firstName: String?,
    var lastName: String?,
    var avatar: String?,
    var rating: Int = 0,
    var respect: Int = 0,
    val lastVisit: Date? = null,
    val isOnline: Boolean = false
) {

    // Вторичный конструктор
    constructor(id: String, firstName: String?, lastName: String?) : this(
        id = id,
        firstName = firstName,
        lastName = lastName,
        avatar = null
    )

    // Вторичный конструктор
    constructor(id: String) : this(id, "John", "Doe")

    // Блок инициализации
    init {
        println(
            "It's Alive!!! \n" +
                    "${if (lastName === "Doe") "His name is ${firstName.orEmpty()} $lastName" else "And his name is ${firstName.orEmpty()} ${lastName.orEmpty()}"}\n"
        )
    }

    companion object Factory {
        private var lastId: Int = -1

        fun makeUser(fullName: String?): User {
            lastId++
            val (firstName, lastName) = Utils.parseFullName(fullName) // деструтуризация объекта
            return User(id = "$lastId", firstName = firstName, lastName = lastName)
        }
    }
}