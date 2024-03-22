package com.wakeupdev.joyfin.utils

import android.content.Context
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.util.Log
import androidx.core.content.ContextCompat
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Arrays
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit


object AppStringUtils {
    val TAG: String = AppStringUtils::class.java.simpleName
    fun trimLast(str: String, character: String): String {
        return str.replace("$character $".toRegex(), "")
    }

    fun customFormat(value: Int): String {
        val pattern = "###,###,###"
        val myFormatter = DecimalFormat(pattern)
        val output = myFormatter.format(value.toLong())
        println("$value  $pattern  $output")
        return output
    }

    fun customAmountFormat(value: Double): SpannableString {
        val output = String.format(Locale.getDefault(), "₦ %,.2f", value)
        val ss1 = SpannableString(output)
        ss1.setSpan(RelativeSizeSpan(1.2f), 0, output.length - 3, 0) // set size
        return ss1
    }

    fun customFormat(value: Long): String {
        val pattern = "###,###,###"
        val myFormatter = DecimalFormat(pattern)
        val output = myFormatter.format(value)
        println("$value  $pattern  $output")
        return output
    }

    fun getItemSummary(packs: Int, units: Int, discount: Double): String {
        return "$packs cases , $units units  , $discount% discount"
    }

    fun customFormat(value: Double): String {
        val pattern = "###,###,###.##"
        val myFormatter = DecimalFormat(pattern)
        val output = myFormatter.format(value)
        println("$value  $pattern  $output")
        Log.d(TAG, "output value : $output")
        return output
    }

    fun customFormat(value: Double, context: Context): String {
        val pattern = "###,###,###.###"
        val myFormatter = DecimalFormat(pattern)
        Log.d(TAG, value.toString() + "  " + pattern + "  " + myFormatter.format(value))
        return String.format(
            "%s%s",
            "#",
            myFormatter.format(value)
        )
    }

    fun timeDiff(time1: String?, time2: String?): Long {
        val format = SimpleDateFormat("HH:mm")
        var difference: Long = 0
        try {
            val date1 = format.parse(time1)
            val date2 = format.parse(time2)!!
            assert(date1 != null)
            difference = date2.time - date1!!.time
            difference = if (difference < 0) -difference else difference
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return difference
    }

    fun convertMillis(time: String?): Long {
        val format = SimpleDateFormat("HH:mm")
        var difference: Long = 0
        try {
            val date1 = format.parse(time)!!
            difference = date1.time
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return difference
    }

    fun convertStringToDate(startDateString: String?): Date? {
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val startDate: Date
        try {
            startDate = df.parse(startDateString)
            return startDate
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return null
    }

    fun convertStringToCalender(startDateString: String?): Calendar {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val calendar = Calendar.getInstance()
        try {
            val parsedDate = dateFormat.parse(startDateString)

            // Convert the Date to a Calendar instance
            calendar.time = parsedDate
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return calendar
    }

    operator fun get(year: Int, month: Int, day: Int): String {
        return "%$year-$month-%"
    }


    fun diffTime(a: Long, b: Long): Long {
        var diff: Long = 0
        val dateFormat = SimpleDateFormat("ss S")
        try {
            val firstParsedDate = dateFormat.parse(a.toString())
            val secondParsedDate = dateFormat.parse(b.toString())
            diff = secondParsedDate.time - firstParsedDate.time
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return diff
    }

    fun formatStringToDate(dateString: String): String{
        try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val dateObj = sdf.parse(dateString) ?: throw Exception("Invalid date format")

            return SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(dateObj)
        } catch (e: Exception){
            return ""
        }


    }
}
