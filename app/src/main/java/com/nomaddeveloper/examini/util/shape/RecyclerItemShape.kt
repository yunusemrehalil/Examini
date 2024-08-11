package com.nomaddeveloper.examini.util.shape

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.widget.FrameLayout

class RecyclerItemShape @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = getRandomColor()
    }

    private val path = Path()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width.toFloat()
        val height = height.toFloat()
        val radius = 100f

        path.apply {
            reset()
            moveTo(0f, radius)
            quadTo(0f, 0f, radius, 0f)
            lineTo(width - radius, 0f)
            quadTo(width, 0f, width, radius)
            lineTo(width, height)
            quadTo(width, height - radius, width - radius, height - radius)
            lineTo(radius, height - radius)
            quadTo(0f, height - radius, 0f, height)
            close()
        }
        canvas.drawPath(path, paint)

    }

    override fun dispatchDraw(canvas: Canvas) {
        val save = canvas.save()
        canvas.clipPath(path)
        super.dispatchDraw(canvas)
        canvas.restoreToCount(save)
    }

    private fun getRandomColor(): Int {
        val random = java.util.Random()
        val color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256))
        return color
    }

}
