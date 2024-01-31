package dev.awd.injaaz.utils

import java.text.SimpleDateFormat
import java.util.Locale

fun extractHourFormatted(date: Long): String {
    val timeFormatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
    return timeFormatter.format(date)
}


fun extractDateFormatted(date: Long): String {
    val dateFormatter = SimpleDateFormat("EEE dd MMM", Locale.getDefault())
    return dateFormatter.format(date)
}