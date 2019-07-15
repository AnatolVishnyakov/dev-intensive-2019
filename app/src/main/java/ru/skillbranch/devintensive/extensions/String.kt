package ru.skillbranch.devintensive.extensions

private val CYRILIC = arrayOf(
    "а","б","в","г","д","е","ё","ж","з","и","й","к","л","м","н","о","п","р","с","т","у","ф","х","ц","ч","ш","щ","ъ","ы","ь","э","ю","я"
)

private val LATIN = arrayOf(
    "a","b","v","g","d","e","e","zh","z","i","i","k","l","m","n","o","p","r","s","t","u","f","h","c","ch","sh","sh'","","i","","e","yu","ya"
)

private const val SYMBOL_NOT_FOUND = -1

fun String.transliterate(): String {
    val sb = StringBuilder()
    for (symbol in this.toCharArray()) {
        val index = CYRILIC.indexOf(symbol.toString().toLowerCase())
        if (index != SYMBOL_NOT_FOUND) {
            if (symbol.isUpperCase()) {
                // символы в верхнем регистре
                sb.append(LATIN[index].capitalize())
            } else {
                // символ в нижнем регистре
                sb.append(LATIN[index])
            }
        } else {
            // символы не из алфавита
            sb.append(symbol)
        }
    }
    return sb.toString()
}

// TODO реализовать
fun String.truncate(num: Int = 16): String {
    var buffer = this.trim()
    if (buffer.length < num) {
        return buffer
    }
    return this.substring(0, num).trim().plus("...")
}

// TODO реализовать
fun String.stripHtml(): String {
    return ""
}