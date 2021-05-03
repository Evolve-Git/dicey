package com.evolve.dicey.logic

import android.animation.*
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.animation.*
import android.widget.TextView
import androidx.core.animation.doOnRepeat
import androidx.core.content.ContextCompat
import com.evolve.dicey.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random


class Dicey(private var context: Context, private val view: TextView){
    private val pref = Prefs(context)
    private val tts = TTS(context)
    private val dicey: Array<String> = context.resources.getStringArray(R.array.dicey)
    private lateinit var animSet: AnimatorSet

    private fun dice(): String{
        return when (val seed = Random.nextInt(6)) {
            0, 3 -> dicey[seed]
            1, 2 -> String.format(dicey[seed], pref.name1)
            4, 5 -> String.format(dicey[seed], pref.name2)
            else -> "Error"
        }
    }

    fun animate(choice: Int){
        //TODO load animation type from prefs? 3x3 settings should look nice
        val interpolator: Interpolator = when (Random.nextInt(5)){
            0 -> OvershootInterpolator()
            1 -> BounceInterpolator()
            2 -> AnticipateInterpolator()
            3 -> AnticipateOvershootInterpolator()
            else -> LinearInterpolator()
        }

        val seed = if (choice == RANDOM){
            Random.nextInt(9)
        }
        else choice

        when (seed){
            FADE        -> fade(interpolator)
            SHUTTER     -> shutter(interpolator)
            SLIDE       -> slide(interpolator)
            MOVE        -> move((Random.nextInt(3)-1).toFloat(),
                            (Random.nextInt(3)-1).toFloat(), interpolator)
            ROTATE      -> rotate(interpolator)
            ROTATEX     -> rotateX(interpolator)
            STRETCH     -> stretch(interpolator)

            TYPEWRITER  -> typewriter()

            COLORS      -> colors()
            else        -> none()
        }
    }

    private fun none(){
        val temp = dice()
        view.text = temp
        if (pref.isTTSon) tts.speak(temp)
    }

    private fun fade(int: Interpolator){
        AnimatorInflater.loadAnimator(context, R.animator.fade).apply {
            interpolator = int
            setTarget(view)
            doOnRepeat {
                val temp = dice()
                view.text = temp
                if (pref.isTTSon) tts.speak(temp)
            }
            start()
        }
    }

    private fun shutter(int: Interpolator){
        AnimatorInflater.loadAnimator(context, R.animator.shutter).apply {
            interpolator = int
            setTarget(view)
            doOnRepeat {
                val temp = dice()
                view.text = temp
                if (pref.isTTSon) tts.speak(temp)
            }
            start()
        }
    }

    private fun slide(int: Interpolator){
        AnimatorInflater.loadAnimator(context, R.animator.slide).apply {
            interpolator = int
            setTarget(view)
            doOnRepeat {
                val temp = dice()
                view.text = temp
                if (pref.isTTSon) tts.speak(temp)
            }
            start()
        }
    }

    private fun move(x: Float, y: Float, int: Interpolator){
        val animax = (AnimatorInflater.loadAnimator(context, R.animator.move_x) as ObjectAnimator).apply {
            interpolator = int
            target = view
            setFloatValues(0f, 2500f * x)
            doOnRepeat {
                val temp = dice()
                view.text = temp
                if (pref.isTTSon) tts.speak(temp)
            }
        }

        val animay = (AnimatorInflater.loadAnimator(context, R.animator.move_y) as ObjectAnimator).apply {
            interpolator = int
            target = view
            setFloatValues(0f, 1000f * y)
        }

        AnimatorSet().apply {
            play(animax).with(animay)
            start()
        }
    }

    private fun rotate(int: Interpolator){
        (AnimatorInflater.loadAnimator(context, R.animator.rotate) as ObjectAnimator).apply {
            interpolator = int
            target = view
            doOnRepeat {
                setFloatValues(180f, 360f)
                val temp = dice()
                view.text = temp
                if (pref.isTTSon) tts.speak(temp)
            }
            start()
        }
    }

    private fun rotateX(int: Interpolator){
        AnimatorInflater.loadAnimator(context, R.animator.rotate_x).apply {
            interpolator = int
            setTarget(view)
            doOnRepeat {
                val temp = dice()
                view.text = temp
                if (pref.isTTSon) tts.speak(temp)
            }
            start()
        }
    }

    private fun stretch(int: Interpolator){
        AnimatorInflater.loadAnimator(context, R.animator.stretch).apply {
            interpolator = int
            setTarget(view)
            doOnRepeat {
                val temp = dice()
                view.text = temp
                if (pref.isTTSon) tts.speak(temp)
            }
            start()
        }
    }

    private fun typewriter(){
        val delta = 50
        val temp = dice()
        view.text = temp
        if (pref.isTTSon) tts.speak(temp)
        GlobalScope.launch(Dispatchers.Main) {
            delay((delta * (temp.length) + 1).toLong())
        }
        for (i in 0..temp.length) {
            GlobalScope.launch(Dispatchers.Main) {
                delay((delta * i).toLong())
                view.text = temp.subSequence(0, i)
            }
        }
    }

    private fun colors(){
        val temp = dice()
        view.text = temp
        if (pref.isTTSon) tts.speak(temp)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ObjectAnimator.ofArgb(view, "TextColor",
                    ContextCompat.getColor(context, R.color.light_blue_A200), Color.MAGENTA,
                    Color.BLUE, Color.CYAN, Color.GREEN,
                    ContextCompat.getColor(context, R.color.light_blue_A200)).apply {
                duration = 1000

                start()
            }
        }
        else {
            (AnimatorInflater.loadAnimator(context, R.animator.color) as AnimatorSet).apply {
                setTarget(view)
                start()
            }
        }
    }

    fun killTTS() {
        tts.kill()
    }
}