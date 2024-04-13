package dev.awd.injaaz.utils

import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Helper function to extract hour and minutes from a date in millis
 * in the given pattern.
 *@return hour extracted in String format
 * @param date the given date in millis
 * @param pattern hour pattern [default: 11:59 AM]
 */
fun extractHourFormattedFromDate(date: Number, pattern: String = "hh:mm a"): String {
    val timeFormatter = SimpleDateFormat(pattern, Locale.getDefault())
    return timeFormatter.format(date)
}

/**
 * Helper function to give a human readable string of a given date in millis.
 * @return date formatted in a String format
 * @param date the given date in millis
 * @param pattern date pattern [default: Mon 1 Feb]
 */
fun formatDate(date: Number, pattern: String = "EEE dd MMM"): String {
    val dateFormatter = SimpleDateFormat(pattern, Locale.getDefault())
    return dateFormatter.format(date)
}

fun getTimeInMillis(hour: Int, minute: Int = 0, second: Int = 0): Long {
    val hoursInMillis = hour.times(60).times(60000)
    val minutesInMillis = minute.times(60000)
    val secondsInMillis = second.times(1000)
    return (hoursInMillis + minutesInMillis + secondsInMillis).toLong()
}