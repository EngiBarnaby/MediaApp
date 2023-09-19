package com.example.myyandexproject.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun convertTime(time : Int) : String{
    return SimpleDateFormat("mm:ss", Locale.getDefault()).format(time)
}

fun getBigImageUrl(url : String?) : String? {
    return url?.replace("100x100bb.jpg", "512x512bb.jpg")
}

fun getYearFromReleaseDate(date : String) : String{
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())

    try {
        val formatDate = dateFormat.parse(date)
        val calendar = Calendar.getInstance()
        calendar.time = formatDate!!

        val year = calendar.get(Calendar.YEAR)
        return year.toString()
    } catch (e: Exception) {
        return "0"
    }
}