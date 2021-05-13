package com.evolve.dicey.logic

import android.annotation.SuppressLint
import com.evolve.dicey.R
import java.text.SimpleDateFormat
import java.util.*

data class GreeterData(val image: Int, val greeting: String)

fun getGreeterData(): GreeterData {
    @SuppressLint("SimpleDateFormat")
    val currentHour = SimpleDateFormat("HH").format(Date()).toInt()

    val morning = arrayOf(
        R.drawable.morning_20190424_210132,
        R.drawable.morning_20190512_160048
    )
    val day = arrayOf(
        R.drawable.day_20180724_140019,
        R.drawable.day_20180911_081046
    )
    val eveing = arrayOf(
        R.drawable.evening_20200822_190246,
        R.drawable.evening_20200901_193621
    )
    val night = arrayOf(
        R.drawable.night_20181102_010555,
        R.drawable.night_20181102_022026,
        R.drawable.night_20181102_031112,
        R.drawable.night_20190417_211804
    )

    return when (currentHour) {
        in 6..11 ->  GreeterData(morning.random(), "morning")
        in 12..17 -> GreeterData(day.random(), "day")
        in 18..23 -> GreeterData(eveing.random(), "evening")
        else ->      GreeterData(night.random(), "night")
    }
}