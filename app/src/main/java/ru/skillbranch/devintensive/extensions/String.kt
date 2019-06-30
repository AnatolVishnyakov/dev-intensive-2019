package ru.skillbranch.devintensive.extensions

import java.lang.StringBuilder

private val CYRILIC = charArrayOf(
    ' ','а','б','в','г','д','е','ё','ж','з','и','й','к','л','м','н','о','п','р','с','т','у','ф','х','ц','ч','ш','щ','ъ','ы','ь','э','ю','я'
)

private val LATIN = arrayOf(
    " ", "a","b","v","g","d","e","e","zh","z","i","y","k","l","m","n","o","p","r","s","t","u","f","h","ts","ch","sh","sch","","i","","e","ju","ja"
)

fun String.transliterate(): String {
    val sb = StringBuilder()
    for (symbol in this.toCharArray()) {
        val index = CYRILIC.indexOf(symbol.toLowerCase())
        if (index != -1) {
            if (symbol.isUpperCase()) {
                sb.append(LATIN[index].toUpperCase())
            } else {
                sb.append(LATIN[index])
            }
        } else {
            sb.append(symbol)
        }
    }
    return sb.toString()
}