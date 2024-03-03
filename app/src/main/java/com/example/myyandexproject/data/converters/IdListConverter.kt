package com.example.myyandexproject.data.converters

import android.util.Log
import androidx.room.TypeConverter

class IdListConverter {
    @TypeConverter
    fun fromString(value: String): List<Int> {
        return if(value.isNotBlank()){
            if(value.first() == ','){
                val newValue = value.substring(1)
                newValue.split(",").map { it.toInt() }
            }
            else{
                value.split(",").map { it.toInt() }
            }

        } else{
            emptyList<Int>()
        }
    }

    @TypeConverter
    fun fromList(list: List<Int>): String {
        val stringIds = list.joinToString(",")
        return stringIds
    }
}