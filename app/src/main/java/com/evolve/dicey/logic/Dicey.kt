package com.evolve.dicey.logic

import android.content.Context
import com.evolve.dicey.R
import kotlin.random.Random

class Dicey(context: Context){
    private val dicey: Array<String> = context.resources.getStringArray(R.array.dicey)
    private val pref = Prefs(context)

    fun dice(): String{
        return when (val seed = Random.nextInt(6)) {
            0, 3 -> dicey[seed]
            1, 2 -> String.format(dicey[seed], pref.name1)
            4, 5 -> String.format(dicey[seed], pref.name2)
            else -> "Error"
        }
    }
}