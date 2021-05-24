package com.evolve.dicey.logic

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.animation.*
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.evolve.dicey.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class AnimsManager(private var context: Context){
    private val pref = Prefs(context)
    private val tts = TTS(context)
    private var animSet = initAnimSet(pref.anims)
    private var animColorSet = initAnimColorSet()

    fun animate(view: TextView, text: String = "") {
        val interpolator: Interpolator = when (Random.nextInt(5)) {
            0 -> OvershootInterpolator()
            1 -> BounceInterpolator()
            2 -> AnticipateInterpolator()
            3 -> AnticipateOvershootInterpolator()
            else -> LinearInterpolator()
        }

        var animTypeWriter = pref.anims[8]
        var animColor = pref.anims.last()

        if (pref.anims[0]) {
            val randomArray = BooleanArray(pref.anims.size) { Random.nextBoolean() }
            animSet = initAnimSet(randomArray)
            animTypeWriter = Random.nextBoolean()
            animColor = Random.nextBoolean()
        }

        animSet.apply {
            setInterpolator(interpolator)
            setTarget(view)
            start()
        }

        if (animColor) {
            animColorSet.apply {
                setTarget(view)
                start()
            }
        }

        if (animTypeWriter) {
            val delta = 1000 / text.length
            if (pref.isTTSon) CoroutineScope(Main).launch {
                delay(500)
                tts.speak(text)
            }
            for (i in 0..text.length) {
                CoroutineScope(Main).launch {
                    delay((delta * i).toLong())
                    view.text = text.subSequence(0, i)
                }
            }
        } else
            CoroutineScope(Main).launch {
                delay(500)
                view.text = text
                if (pref.isTTSon) tts.speak(text)
            }
        }

    private fun initAnimSet(choice: BooleanArray): AnimatorSet {
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

    @SuppressLint("Recycle", "ObjectAnimatorBinding")
    private fun initAnimColorSet(): AnimatorSet {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AnimatorSet().apply { play(
                ObjectAnimator.ofArgb(null, "TextColor",
                ContextCompat.getColor(context, R.color.light_blue_A200), Color.MAGENTA,
                Color.BLUE, Color.CYAN, Color.GREEN,
                ContextCompat.getColor(context, R.color.light_blue_A200)).setDuration(1100))}
        }
        else {
            (AnimatorInflater.loadAnimator(context, R.animator.color) as AnimatorSet)
        }
    }

    fun killTTS() {
        tts.kill()
    }
}