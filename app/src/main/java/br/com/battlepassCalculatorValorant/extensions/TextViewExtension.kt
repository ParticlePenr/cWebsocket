package br.com.battlepassCalculatorValorant.extensions.bindingAdapters

import android.content.res.Resources
import android.graphics.Color
import android.graphics.Paint
import android.util.TypedValue
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.databinding.BindingAdapter
import br.com.battlepassCalculatorValorant.R


@BindingAdapter("adiantado")
fun TextView.adiantado(sucess: Boolean) {
    if (sucess) {
        setTextColor(Color.parseColor("#4CD964"))
    } else {
        setTextColor(Color.parseColor("#E69700"))
    }
}

@BindingAdapter("fadeAnimationText")
fun TextView.fadeAnimationText(newText: String?) {
    if (newText != null) {
        val anim = AlphaAnimation(1.0f, 0.0f)
        anim.duration = 300
        anim.repeatCount = 1
        anim.repeatMode = Animation.REVERSE

        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationEnd(animation: Animation?) {}
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationRepeat(animation: Animation?) {
                text = newText
            }
        })
        startAnimation(anim)
    } else {
        text = newText
    }

}

@BindingAdapter("position", "positionCurrent")
fun TextView.comparableStyle(position: Int?, positionCurrent: Int?) {
    if (position != null && positionCurrent != null) {
        val gray = getColor(R.attr.colorOnSecondary)
        val white = getColor(R.attr.colorOnPrimary)
        val accent = getColor(R.attr.colorAccent)

        setTextColor(white)
        paintFlags = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        if (position < positionCurrent) {
            paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            setTextColor(gray)
        } else if (position == positionCurrent) {
            setTextColor(accent)
        }
    }
}

@ColorInt
fun View.getColor(@AttrRes resId: Int): Int {
    val typedValue = TypedValue()
    val theme: Resources.Theme = context.theme
    theme.resolveAttribute(resId, typedValue, true)
    return typedValue.data
}