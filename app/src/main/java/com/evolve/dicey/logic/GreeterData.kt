package com.evolve.dicey.logic

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import com.evolve.dicey.R
import java.text.SimpleDateFormat
import java.util.Date

    @SuppressLint("SimpleDateFormat")
    private val currentHour = SimpleDateFormat("HH").format(Date()).toInt()
    private val timeOfDay = getTOfD()

    private val morning = arrayOf(
        R.drawable.morning_20190424_210132,
        R.drawable.morning_20190512_160048
    )
    private val day = arrayOf(
        R.drawable.day_20180724_140019,
        R.drawable.day_20180911_081046
    )
    private val evening = arrayOf(
        R.drawable.evening_20200822_190246,
        R.drawable.evening_20200901_193621
    )
    private val night = arrayOf(
        R.drawable.night_20181102_010555,
        R.drawable.night_20181102_022026,
        R.drawable.night_20181102_031112,
        R.drawable.night_20190417_211804
    )
    private val imageArray = arrayOf(morning.random(), day.random(), evening.random(), night.random())

    fun getGreeterImage(): Int {
        return imageArray[timeOfDay]
    }

    fun Fragment.getGreeterString(): String{
        val greetings = resources.getStringArray(R.array.greetings)
        val tOd1 = resources.getStringArray(R.array.good_time_of_day)
        val tOd2 = resources.getStringArray(R.array.this_time_of_day)
        val tOd3 = resources.getStringArray(R.array.time_of_day)
        val tOd4 = resources.getStringArray(R.array.it_is_time_of_day)
        return String.format(greetings.random(), tOd1[timeOfDay], tOd2[timeOfDay],
            tOd3[timeOfDay], tOd4[timeOfDay])
    }

    private fun getTOfD(): Int {
        return when (currentHour) {
            in 6..11 ->  0
            in 12..17 -> 1
            in 18..23 -> 2
            else ->      3
        }
    }