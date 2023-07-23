package com.example.myyandexproject.utils

import java.text.SimpleDateFormat
import java.util.Locale

fun convertTime(time : Int) : String{
    return SimpleDateFormat("mm:ss", Locale.getDefault()).format(time)
}