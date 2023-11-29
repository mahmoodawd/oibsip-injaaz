package dev.awd.injaaz.utils

import java.util.Locale

fun String.capitalize(): String = replaceFirstChar {
    if (it.isLowerCase()) it.titlecase(
        Locale.ROOT
    ) else it.toString()
}