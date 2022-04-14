/**View для первой страницы со статьями*/
package ru.dm.android.truestyle.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.util.AttributeSet
import android.view.View
import ru.dm.android.truestyle.R


private const val TAG = "RecommendedArticleView"

class RecommendedArticleView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        var paint: Paint = Paint()
        paint.shader = LinearGradient(0f, 0f,
            0f, height.toFloat(),
            resources.getColor(R.color.transparent), resources.getColor(R.color.transparent_black),
            Shader.TileMode.MIRROR)
        canvas?.drawPaint(paint)
    }

}