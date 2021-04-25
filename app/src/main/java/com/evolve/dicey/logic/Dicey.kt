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


class Dicey(private val context: Context, private val view: TextView){
    //TODO move tts here?
    private val pref = Prefs(context)
    private val dicey: Array<String> = context.resources.getStringArray(R.array.dicey)

    private fun dice(): String{
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
        when (choice){
            0 -> fade(interpolator)
            1 -> shutter(interpolator)
            2 -> slide(interpolator)
            3 -> move((Random.nextInt(3)-1).toFloat(),
                    (Random.nextInt(3)-1).toFloat(), interpolator)
            4 -> rotate(interpolator)
            5 -> colors()
            6 -> typewriter()
            7 -> stretch(interpolator)
            else -> none()
        }
    }

    private fun none(){
        view.text = dice()
    }

    private fun fade(int: Interpolator){
        val anima = AnimatorInflater.loadAnimator(context, R.animator.fade)
        anima.interpolator = int
        anima.setTarget(view)
        anima.doOnRepeat { view.text = dice() }
        anima.start()
    }

    private fun shutter(int: Interpolator){
        val anima = AnimatorInflater.loadAnimator(context, R.animator.shutter)
        anima.interpolator = int
        anima.setTarget(view)
        anima.doOnRepeat { view.text = dice() }
        anima.start()
    }

    private fun slide(int: Interpolator){
        val anima = AnimatorInflater.loadAnimator(context, R.animator.slide)
        anima.interpolator = int
        anima.setTarget(view)
        anima.doOnRepeat { view.text = dice() }
        anima.start()
    }

    private fun move(x: Float, y: Float, int: Interpolator){
        //TODO proper return animation for maximum interpolation coolness
        val animax = AnimatorInflater.loadAnimator(context, R.animator.move_x) as ObjectAnimator
        animax.interpolator = int
        animax.target = view
        animax.setFloatValues(2500f*x)
        animax.doOnRepeat { view.text = dice() }

        val animay = AnimatorInflater.loadAnimator(context, R.animator.move_y) as ObjectAnimator
        animay.interpolator = int
        animay.target = view
        animay.setFloatValues(1000f*y)

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
            view.text = dice() }
        anima.start()
    }

    private fun colors(){
        view.text = dice()
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

    private fun typewriter(){
        val delta = 55
        val temp = dice()
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

    private fun stretch(int: Interpolator){
        val anima = AnimatorInflater.loadAnimator(context, R.animator.stretch)
        anima.interpolator = int
        anima.setTarget(view)
        anima.doOnRepeat { view.text = dice() }
        anima.start()
    }
}