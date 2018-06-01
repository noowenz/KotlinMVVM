package au.com.official.nwz.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

const val FORMAT_Y_M_D: String = "yyyy-MM-dd"
const val FORMAT_MONTH: String = "MMMM"
const val FORMAT_MONTH_YEAR_SHORT: String = "MMM yyyy"
const val FORMAT_Y_M: String = "yyyy-MM"
const val FORMAT_DAY_MONTH_YEAR: String = "dd MMMM yyyy"
const val FORMAT_MONTH_YEAR: String = "MMMM yyyy"
const val FORMAT_ADD_INFRINGEMENT_DISPLAY: String = "EEEE dd MMMM, HH:mm"
const val FORMAT_NOTIFICATION_DISPLAY: String = "EEEE dd MMMM yyyy, HH:mm"
const val FORMAT_FULL_DATE: String = "yyyy-MM-dd HH:mm:ss"
const val FORMAT_LIST_INFRINGEMENT: String = "hh:mma, dd MMM yyyy" //09:00AM, 12 Dec 2017

fun convertDateToString(date: Date?, format: String = FORMAT_DAY_MONTH_YEAR): String {
    return if (date != null) {
        val dateFormat = SimpleDateFormat(format, Locale.getDefault())
        dateFormat.format(date)
    } else {
        ""
    }
}

fun convertDateToString(string: String, sourceFormat: String = FORMAT_Y_M_D, resultFormat: String = FORMAT_DAY_MONTH_YEAR): String {
    val sdf = SimpleDateFormat(sourceFormat, Locale.getDefault())
    return try {
        val date = sdf.parse(string)
        convertDateToString(date, resultFormat)
    } catch (e: ParseException) {
        e.printStackTrace()
        ""
    }
}

fun getDisplayDateForInfringement(date: Date?): String {
    return if (date != null) {
        val dateFormat = SimpleDateFormat(FORMAT_ADD_INFRINGEMENT_DISPLAY, Locale.getDefault())
        dateFormat.format(date)
    } else {
        ""
    }
}

fun convertLocalDateToUtcString(date: Date?, format: String = FORMAT_FULL_DATE): String {
    return if (date != null) {
        val dateFormat = SimpleDateFormat(format, Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        dateFormat.format(date)
    } else {
        ""
    }
}

fun convertUtcStringToLocalString(string: String, sourceFormat: String = FORMAT_Y_M_D, resultFormat: String = FORMAT_DAY_MONTH_YEAR): String {
    val sdf = SimpleDateFormat(sourceFormat, Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone("UTC")

    return try {
        val date = sdf.parse(string)
        convertUtcDateToLocalString(date, resultFormat)
    } catch (e: ParseException) {
        e.printStackTrace()
        ""
    }
}

fun convertUtcDateToLocalString(date: Date?, format: String = FORMAT_DAY_MONTH_YEAR): String {
    return if (date != null) {
        val dateFormat = SimpleDateFormat(format, Locale.getDefault())
        dateFormat.timeZone = TimeZone.getDefault()
        dateFormat.format(date)
    } else {
        ""
    }
}

fun getMonthNameForIncomeSummary(string: String): String {
    val sdf = SimpleDateFormat(FORMAT_Y_M_D, Locale.getDefault())
    return try {
        val date = sdf.parse(string)
        val sdfResult = SimpleDateFormat(FORMAT_MONTH, Locale.getDefault())
        sdfResult.format(date)
    } catch (e: ParseException) {
        e.printStackTrace()
        ""
    }
}

fun getMonthNameAndYear(string: String, sourceFormat: String = FORMAT_Y_M_D): String {
    val sdf = SimpleDateFormat(sourceFormat, Locale.getDefault())
    return try {
        val date = sdf.parse(string)
        val sdfResult = SimpleDateFormat(FORMAT_MONTH_YEAR_SHORT, Locale.getDefault())
        sdfResult.format(date)
    } catch (e: ParseException) {
        e.printStackTrace()
        ""
    }
}

fun Calendar.getOffsetInSecs(): String {
    return TimeUnit.MILLISECONDS.toSeconds(timeZone.getOffset(timeInMillis).toLong()).toString()
}

fun Calendar.getStartingDateForWallet(): String {
    return if (get(Calendar.MONTH) < 7) {//Starting month for wallet is july
        "Jul ${get(Calendar.YEAR) - 1}"
    } else {
        "Jul ${get(Calendar.YEAR)}"
    }
}

fun getTimeDifference(string: String): String {
    val sdf = SimpleDateFormat(FORMAT_FULL_DATE, Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone("UTC")

    return try {
        val utcTime = sdf.parse(string).time

        val localUtcDate = sdf.format(Calendar.getInstance().timeInMillis)
        val currentTimeInUtc = sdf.parse(localUtcDate).time

        val seconds = TimeUnit.MILLISECONDS.toSeconds(currentTimeInUtc - utcTime)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(currentTimeInUtc - utcTime)
        val hours = TimeUnit.MILLISECONDS.toHours(currentTimeInUtc - utcTime)
        val days = TimeUnit.MILLISECONDS.toDays(currentTimeInUtc - utcTime)
        val months = days.div(12)
        return when {
            seconds < 60 -> "Just now"
            minutes == 1.toLong() -> "$minutes minute ago"
            minutes < 60 -> "$minutes minutes ago"
            hours == 1.toLong() -> "$hours hour ago"
            hours < 24 -> "$hours hours ago"
            days == 1.toLong() -> "$days day ago"
            days < 30 -> "$days days ago"
            months == 1.toLong() -> "$months month ago"
            months < 12 -> "$months months ago"
            else -> convertUtcStringToLocalString(string, sourceFormat = FORMAT_FULL_DATE, resultFormat = FORMAT_NOTIFICATION_DISPLAY)
        }
    } catch (exception: Exception) {
        ""
    }
}





