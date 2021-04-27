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

    private fun dice(): String{
        setLocale(context)
        val dicey: Array<String> = context.resources.getStringArray(R.array.dicey)
        return when (val seed = Random.nextInt(6)) {
            0, 3 -> dicey[seed]
            1, 2 -> String.format(dicey[seed], pref.n1)
            4, 5 -> String.format(dicey[seed], pref.n2)
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
            FADE -> fade(interpolator)
            SHUTTER -> shutter(interpolator)
            SLIDE -> slide(interpolator)
            MOVE -> move((Random.nextInt(3)-1).toFloat(),
                    (Random.nextInt(3)-1).toFloat(), interpolator)
            ROTATE -> rotate(interpolator)
            ROTATEX -> rotateX(interpolator)
            STRETCH -> stretch(interpolator)

            TYPEWRITER -> typewriter()

            COLORS -> colors()
            else -> none()
        }
    }

    private fun none(){
        val temp = dice()
        view.text = temp
        if (pref.isTTSon) tts.speak(temp)
    }

    private fun fade(int: Interpolator){
        val anima = AnimatorInflater.loadAnimator(context, R.animator.fade)
        anima.interpolator = int
        anima.setTarget(view)
        anima.doOnRepeat {
            val temp = dice()
            view.text = temp
            if (pref.isTTSon) tts.speak(temp)}
        anima.start()
    }

    private fun shutter(int: Interpolator){
        val anima = AnimatorInflater.loadAnimator(context, R.animator.shutter)
        anima.interpolator = int
        anima.setTarget(view)
        anima.doOnRepeat {
            val temp = dice()
            view.text = temp
            if (pref.isTTSon) tts.speak(temp) }
        anima.start()
    }

    private fun slide(int: Interpolator){
        val anima = AnimatorInflater.loadAnimator(context, R.animator.slide)
        anima.interpolator = int
        anima.setTarget(view)
        anima.doOnRepeat {
            val temp = dice()
            view.text = temp
            if (pref.isTTSon) tts.speak(temp) }
        anima.start()
    }

    private fun move(x: Float, y: Float, int: Interpolator){
        val animax = AnimatorInflater.loadAnimator(context, R.animator.move_x) as ObjectAnimator
        animax.interpolator = int
        animax.target = view
        animax.setFloatValues(0f, 2500f*x)
        animax.doOnRepeat {
            val temp = dice()
            view.text = temp
            if (pref.isTTSon) tts.speak(temp) }

        val animay = AnimatorInflater.loadAnimator(context, R.animator.move_y) as ObjectAnimator
        animay.interpolator = int
        animay.target = view
        animay.setFloatValues(0f, 1000f*y)

        val animSet = AnimatorSet()
        animSet.play(animax).with(animay)
        animSet.start()
    }

    private fun rotate(int: Interpolator){
        val anima = AnimatorInflater.loadAnimator(context, R.animator.rotate) as ObjectAnimator
        anima.interpolator = int
        anima.target = view
        anima.doOnRepeat {
            anima.setFloatValues(180f, 360f)
            val temp = dice()
            view.text = temp
            if (pref.isTTSon) tts.speak(temp) }
        anima.start()
    }

    private fun rotateX(int: Interpolator){
        val anima = AnimatorInflater.loadAnimator(context, R.animator.rotate_x)
        anima.interpolator = int
        anima.setTarget(view)
        anima.doOnRepeat {
            val temp = dice()
            view.text = temp
            if (pref.isTTSon) tts.speak(temp) }
        anima.start()
    }

    private fun stretch(int: Interpolator){
        val anima = AnimatorInflater.loadAnimator(context, R.animator.stretch)
        anima.interpolator = int
        anima.setTarget(view)
        anima.doOnRepeat {
            val temp = dice()
            view.text = temp
            if (pref.isTTSon) tts.speak(temp) }
        anima.start()
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
            val anima = ObjectAnimator.ofArgb(view, "TextColor",
                    ContextCompat.getColor(context, R.color.light_blue_A200), Color.MAGENTA,
                    Color.BLUE, Color.CYAN, Color.GREEN,
                    ContextCompat.getColor(context, R.color.light_blue_A200))
            anima.duration = 1000

            anima.start()
        }
        else {
            val anima = AnimatorInflater.loadAnimator(context, R.animator.color) as AnimatorSet
            anima.setTarget(view)
            anima.start()
        }
    }

    fun killTTS() {
        tts.kill()
    }
}