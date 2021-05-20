package com.evolve.dicey.logic

import android.animation.*
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.animation.*
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.evolve.dicey.R
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class Dicey(private var context: Context, private val view: TextView){
    private val pref = Prefs(context)
    private val tts = TTS(context)
    private val dicey: Array<String> = context.resources.getStringArray(R.array.dicey)
    private var animSet = initAnimSet(pref.anims)
    private var animColorSet = initAnimColorSet()

    private fun dice(): String{
        return when (val seed = Random.nextInt(6)) {
            0, 3 -> dicey[seed]
            1, 2 -> String.format(dicey[seed], pref.name1)
            4, 5 -> String.format(dicey[seed], pref.name2)
            else -> "Error"
        }
    }

    fun animate(){
        val interpolator: Interpolator = when (Random.nextInt(5)){
            0 -> OvershootInterpolator()
            1 -> BounceInterpolator()
            2 -> AnticipateInterpolator()
            3 -> AnticipateOvershootInterpolator()
            else -> LinearInterpolator()
        }

        var tw = pref.anims[8]
        var color = pref.anims.last()

        if (pref.anims[0]){
            val randomArray = BooleanArray(pref.anims.size) { Random.nextBoolean() }
            animSet = initAnimSet(randomArray)
            tw = Random.nextBoolean()
            color = Random.nextBoolean()
        }

        animSet.apply {
            setInterpolator(interpolator)
            setTarget(view)
            start()
        }

        if (color) {
            animColorSet.start()
        }

        val temp = dice()
        if (tw) {
            val delta = 1000/ temp.length
            if (pref.isTTSon) GlobalScope.launch(Main) {
                delay(500)
                tts.speak(temp)
            }
            for (i in 0..temp.length) {
                GlobalScope.launch(Main) {
                    delay((delta * i).toLong())
                    view.text = temp.subSequence(0, i)
                }
            }
        } else
        GlobalScope.launch(Main) {
            delay(500)
            view.text = temp
            if (pref.isTTSon) tts.speak(temp)
        }
    }

    private fun initAnimSet(choice: BooleanArray): AnimatorSet{
        val animsList = listOf<Animator>(
            AnimatorInflater.loadAnimator(context, R.animator.fade),
            AnimatorInflater.loadAnimator(context, R.animator.shutter),
            AnimatorInflater.loadAnimator(context, R.animator.slide),
            (AnimatorInflater.loadAnimator(context, R.animator.move_x) as ObjectAnimator).apply {
                setFloatValues(0f, 2500f * (Random.nextInt(3)-1).toFloat())
            },
            (AnimatorInflater.loadAnimator(context, R.animator.move_y) as ObjectAnimator).apply {
                setFloatValues(0f, 1000f * (Random.nextInt(3)-1).toFloat())
            },
            AnimatorInflater.loadAnimator(context, R.animator.rotate),
            AnimatorInflater.loadAnimator(context, R.animator.rotate_x)
        )

        val animsListFinal = mutableListOf<Animator>()
        for (i in (1 until choice.size- 2)){
            if (choice[i]) animsListFinal.add(animsList[i- 1])
        }
        return AnimatorSet().apply { playTogether(animsListFinal) }
    }

    @SuppressLint("Recycle")
    private fun initAnimColorSet(): AnimatorSet{
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AnimatorSet().apply { play(ObjectAnimator.ofArgb(view, "TextColor",
                    ContextCompat.getColor(context, R.color.light_blue_A200), Color.MAGENTA,
                    Color.BLUE, Color.CYAN, Color.GREEN,
                    ContextCompat.getColor(context, R.color.light_blue_A200)).setDuration(1000))}
        }
        else {
            (AnimatorInflater.loadAnimator(context, R.animator.color) as AnimatorSet).apply {
                setTarget(view)
            }
        }
    }

    fun killTTS() {
        tts.kill()
    }
}