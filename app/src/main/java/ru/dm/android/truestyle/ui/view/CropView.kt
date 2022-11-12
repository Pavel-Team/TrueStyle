/**Кастомное View для обрезания фото*/
package ru.dm.android.truestyle.ui.view

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import androidx.constraintlayout.widget.ConstraintLayout
import ru.dm.android.truestyle.model.Box


private const val TAG = "CropView"

class CropView(context: Context, attrs: AttributeSet? = null) : androidx.appcompat.widget.AppCompatImageView(context, attrs){

    private var currentBox: Box? = null      //Текущий прямоугольник
    private val boxPaint = Paint().apply {   //Кисть для рисования прямоугольника
        color = 0x60ffffff.toInt()
    }
    private val boxBackground = Paint().apply {   //Кисть для рисования прямоугольника
        color = 0x60000000.toInt()
    }
    var bitmapWidth : Int? = null
    var bitmapHeight : Int? = null
    var isAntiBag = true


    //Получить Box из выделенной картинки
    fun getCropBitmap(): Bitmap? {
        if (currentBox == null)
            return null
        else {
            val bitmap = (this.drawable as BitmapDrawable).bitmap
            val scale = bitmap.width.toFloat() / measuredWidth

            val startX = if (currentBox!!.left < 0) 0 else  (currentBox!!.left * scale).toInt()
            val startY = if (currentBox!!.top < 0) 0 else (currentBox!!.top * scale).toInt()
            val deltaX = if (currentBox!!.right * scale > bitmap.width) (bitmap.width - (currentBox!!.left * scale)).toInt() else ((currentBox!!.right - currentBox!!.left) * scale).toInt()
            val deltaY = if (currentBox!!.bottom * scale > bitmap.height) (bitmap.height - (currentBox!!.top * scale)).toInt() else ((currentBox!!.bottom - currentBox!!.top) * scale).toInt()

            return Bitmap.createBitmap(
                bitmap,
                startX,
                startY,
                deltaX,
                deltaY,
                Matrix(),
                true
            )
        }
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        if (isAntiBag && bitmapWidth != null && measuredWidth != 0) {
            var layoutParams: ConstraintLayout.LayoutParams? = null
            Log.d(TAG, "measuredHeight.toFloat() / measuredWidth = ${measuredHeight.toFloat() / measuredWidth}")
            Log.d(TAG, "bitmapHeight!!.toFloat()/bitmapWidth!! = ${bitmapHeight!!.toFloat()/bitmapWidth!!}")
            val dpi = measuredHeight.toFloat() / measuredWidth

            if (bitmapHeight!!.toFloat()/bitmapWidth!! <= dpi) {
                Log.d(TAG, "width = match_parent")
                layoutParams = ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
                )
                layoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                layoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
            } else {
                Log.d(TAG, "height = match_parent")
                layoutParams = ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.MATCH_PARENT
                )
                layoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                layoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
            }
            this.layoutParams = layoutParams
            isAntiBag = false
        }
    }


    override fun setImageBitmap(bm: Bitmap?) {
        super.setImageBitmap(bm)

        bitmapWidth = bm?.width
        bitmapHeight = bm?.height
        Log.d(TAG, "this.height = ${this.height}")
        Log.d(TAG, "this.measuredHeight = ${this.measuredHeight}")
        Log.d(TAG, "rootView.height = ${this.rootView.height}")
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

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

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
    }

}