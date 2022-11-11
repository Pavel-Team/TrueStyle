/**Кастомное View для обрезания фото*/
package ru.dm.android.truestyle.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import ru.dm.android.truestyle.model.Box

private const val TAG = "CropView"

class CropView(context: Context, attrs: AttributeSet? = null) : View(context, attrs){

    private var currentBox: Box? = null      //Текущий прямоугольник
    private val boxPaint = Paint().apply {   //Кисть для рисования прямоугольника
        color = 0x60ffffff.toInt()
    }
    private val boxBackground = Paint().apply {   //Кисть для рисования прямоугольника
        color = 0x60000000.toInt()
    }


    //Получить Box из выделенной картинки
    fun getCropBox(): Box? {
        return currentBox
    }


    override fun onDraw(canvas: Canvas) {
        canvas.drawPaint(boxBackground)
        if (currentBox != null)
            canvas.drawRect(currentBox!!.left, currentBox!!.top, currentBox!!.right, currentBox!!.bottom, boxPaint)
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        val current = PointF(event.x, event.y) //Самое текущее касание

        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                //Определяем currentBox
                currentBox = Box(current)
            }

            MotionEvent.ACTION_MOVE -> {
                //Обновляем конечную коордианту текущего currentBox и делаем переотрисовку
                updateCurrentBox(current)
            }

            MotionEvent.ACTION_UP -> {
                updateCurrentBox(current)
            }

            MotionEvent.ACTION_CANCEL -> {
                //currentBox = null
            }

        }

        return true
    }


    private fun updateCurrentBox(current: PointF) {
        currentBox?.let {
            it.end = current
            invalidate()
        }
    }

}