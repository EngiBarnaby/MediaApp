package com.example.myyandexproject.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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

fun <T> debounce(delayMillis: Long,
                 coroutineScope: CoroutineScope,
                 useLastParam: Boolean,
                 action: (T) -> Unit): (T) -> Unit {
    var debounceJob: Job? = null
    return { param: T ->
        if (useLastParam) {
            debounceJob?.cancel()
        }
        if (debounceJob?.isCompleted != false || useLastParam) {
            debounceJob = coroutineScope.launch {
                delay(delayMillis)
                action(param)
            }
        }
    }
}