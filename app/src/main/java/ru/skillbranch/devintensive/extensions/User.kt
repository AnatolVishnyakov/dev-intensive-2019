package ru.skillbranch.devintensive.extensions

import ru.skillbranch.devintensive.models.User
import ru.skillbranch.devintensive.models.UserView
import ru.skillbranch.devintensive.utils.Utils
import java.util.*

fun User.toUserView(): UserView {

    val nickname = Utils.transliteration("$firstName $lastName")
    val initials = Utils.toInitials(firstName, lastName)
    val status =
        if (lastVisit == null) "Ещё ни разу не был" else if (isOnline) "online" else "Последний раз был ${lastVisit.humanizeDiff()} назад"

    return UserView(
        id,
        fullName = "$firstName $lastName",
        nickName = nickname,
        initials = initials,
        avatar = "$avatar",
        status = status
    )
}

fun Date.humanizeDiff(date: Date = Date()): String {
/*
    return when (val diffTime = date.time - this.time) {
        in 0..10 * SECOND -> "только что"


        in 2 * MINUTE..9 * MINUTE -> "несколько минут"

        1 * MINUTE -> "минуту"
//        in 10 * MINUTE..20 * MINUTE -> "${diffTime / MINUTE} минут"
//        in 21 * MINUTE..21 * MINUTE + 59 * SECOND -> "${diffTime / MINUTE} минута"
//        in 22 * MINUTE..24 * MINUTE -> "${diffTime / MINUTE} минуты"
//        in 25 * MINUTE..30 * MINUTE -> "${diffTime / MINUTE} минут"

//        in MINUTE..HOUR -> "${diffTime / MINUTE} мин."
//        in HOUR..DAY -> "${diffTime / HOUR} ч."
        else -> "${diffTime / DAY} дн."
    }
*/
    return "TODO time period"
}
