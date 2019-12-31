package com.divyanshu.colorseekbar

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ArrayRes


class ColorSeekBar(context: Context, attributeSet: AttributeSet): View(context, attributeSet){

    private val minThumbRadius = 16f
    private var colorSeeds = intArrayOf(Color.parseColor("#4caf50"),Color.parseColor("#4caf50"),
            Color.parseColor("#2196f3"), Color.parseColor("#673ab7"),
            Color.parseColor("#f20f55"), Color.parseColor("#ff772e"), Color.parseColor("#ff772e"))
    private var canvasHeight: Int = 60
    private var barHeight: Int = 20
    private var rectf: RectF = RectF()
    private var rectPaint: Paint = Paint()
    private var thumbBorderPaint: Paint = Paint()
    private var thumbPaint: Paint = Paint()
    private lateinit var colorGradient: LinearGradient
    private var thumbX: Float = 24f
    private var thumbY: Float = (canvasHeight/2).toFloat()
    private var thumbBorder: Float = 4f
    private var thumbRadius: Float = 16f
    private var thumbBorderRadius: Float = thumbRadius + thumbBorder
    private var thumbBorderColor = Color.BLACK
    private var paddingStart = 60f
    private var paddingEnd = 60f
    private var  paint = Paint()
    val res: Resources = resources
    private var barCornerRadius: Float = 8f
    private var oldThumbRadius = thumbRadius
    private var oldThumbBorderRadius = thumbBorderRadius
    private var colorChangeListener: OnColorChangeListener? = null
    val eyeImages= intArrayOf(R.drawable.eye,R.drawable.half_closed_eye,R.drawable.closed_eye)
    var eyeValue=0
    init {
        attributeSet.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.ColorSeekBar)
            val colorsId = typedArray.getResourceId(R.styleable.ColorSeekBar_colorSeeds, 0)
            if (colorsId != 0) colorSeeds = getColorsById(colorsId)
            barCornerRadius = typedArray.getDimension(R.styleable.ColorSeekBar_cornerRadius, 8f)
            barHeight = typedArray.getDimension(R.styleable.ColorSeekBar_barHeight, 180f).toInt()
            thumbBorder = typedArray.getDimension(R.styleable.ColorSeekBar_thumbBorder, 4f)
            thumbBorderColor = typedArray.getColor(R.styleable.ColorSeekBar_thumbBorderColor, Color.BLACK)
            typedArray.recycle()
        }
        rectPaint.isAntiAlias = true

        thumbBorderPaint.isAntiAlias = true
        thumbBorderPaint.color = thumbBorderColor

        thumbPaint.isAntiAlias = true

        thumbRadius = (barHeight / 2).toFloat().let {
            if (it < minThumbRadius) minThumbRadius else it
        }
        thumbBorderRadius = thumbRadius + thumbBorder
        canvasHeight = (thumbBorderRadius * 3).toInt()
        thumbY = (canvasHeight/2).toFloat()

        oldThumbRadius = thumbRadius
        oldThumbBorderRadius = thumbBorderRadius
    }

    private fun getColorsById(@ArrayRes id: Int): IntArray {
        if (isInEditMode) {
            val s = context.resources.getStringArray(id)
            val colors = IntArray(s.size)
            for (j in s.indices) {
                colors[j] = Color.parseColor(s[j])
            }
            return colors
        } else {
            val typedArray = context.resources.obtainTypedArray(id)
            val colors = IntArray(typedArray.length())
            for (j in 0 until typedArray.length()) {
                colors[j] = typedArray.getColor(j, Color.BLACK)
            }
            typedArray.recycle()
            return colors
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        //color bar position
        val barLeft: Float = paddingStart
        val barRight: Float = width.toFloat() - paddingEnd
        val barTop: Float = ((canvasHeight / 2) - (barHeight / 10)).toFloat()
        val barBottom: Float = ((canvasHeight / 2) + (barHeight / 10)).toFloat()

        //draw color bar
        rectf.set(barLeft, barTop,barRight,barBottom)
        canvas?.drawRoundRect(rectf, barCornerRadius, barCornerRadius, rectPaint)

        if (thumbX < barLeft){
            thumbX = barLeft
        }else if (thumbX > barRight){
            thumbX = barRight
        }
        val color = pickColor(thumbX, width)
        thumbPaint.color = color
        // draw color bar thumb
       // canvas?.drawCircle(thumbX, thumbY, thumbBorderRadius, thumbBorderPaint)
      //  canvas?.drawCircle(thumbX, thumbY, thumbRadius, thumbPaint)
        val filter: ColorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
        paint.colorFilter = filter
        val chamBitmap = BitmapFactory.decodeResource(res, R.drawable. cham)
        val eyeBitmap = BitmapFactory.decodeResource(res, eyeImages[eyeValue])
        val resizedBitmap = Bitmap.createScaledBitmap(eyeBitmap, (chamBitmap.height*0.15).toInt(),(chamBitmap.height*0.15).toInt(), false)

        canvas?.drawBitmap(chamBitmap, thumbX- (paddingEnd+paddingStart), -(chamBitmap.height*0.04f), paint)


        canvas?.drawBitmap(resizedBitmap, thumbX- (paddingEnd+paddingStart)+(chamBitmap.width*0.7f), -(chamBitmap.height*0.04f)+(chamBitmap.height*0.17f), thumbPaint)
    }
    private fun pickColor(position: Float, canvasWidth: Int): Int {
        val value = (position - paddingStart) / (canvasWidth - (paddingStart + paddingEnd))

        colorChangeListener?.onPositionChangeListener((value*100).toInt())
        when {
            value <= 0.0 -> return colorSeeds[0]
            value >= 1 -> return colorSeeds[colorSeeds.size - 1]
            else -> {
                var colorPosition = value * (colorSeeds.size - 1)
                val i = colorPosition.toInt()
                colorPosition -= i
                val c0 = colorSeeds[i]
                val c1 = colorSeeds[i + 1]

                val red = mix(Color.red(c0), Color.red(c1), colorPosition)
                val green = mix(Color.green(c0), Color.green(c1), colorPosition)
                val blue = mix(Color.blue(c0), Color.blue(c1), colorPosition)
                return Color.rgb( red, green, blue)
            }
        }
    }

    private fun mix(start: Int, end: Int, position: Float): Int {
        return start + Math.round(position * (end - start))
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        colorGradient = LinearGradient(0f, 0f, w.toFloat(), 0f, colorSeeds, null, Shader.TileMode.CLAMP)
        rectPaint.shader = colorGradient
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(widthMeasureSpec, canvasHeight)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action){
            MotionEvent.ACTION_DOWN -> {
            colorChangeListener?.onRelease()

            }
            MotionEvent.ACTION_MOVE -> {
                parent.requestDisallowInterceptTouchEvent(true)
                event.x.let {
                    var horizontalLimit=(it/10).toInt()
                    if (horizontalLimit in 17..92 ){
                    thumbX = it
                    invalidate()
                    }
                }
                colorChangeListener?.onColorChangeListener(getColor())
            }
            MotionEvent.ACTION_UP -> {
           //     thumbBorderRadius = oldThumbBorderRadius
            //    thumbRadius = oldThumbRadius
                invalidate()
            }
        }
        return true
    }

    fun getColor()  = thumbPaint.color

    fun setOnColorChangeListener(onColorChangeListener: OnColorChangeListener) {
        this.colorChangeListener = onColorChangeListener
    }

    fun animateEye(){

        if (eyeValue<eyeImages.size-1){
            eyeValue++
        }else{
            eyeValue=0
        }
        invalidate()
    }
    interface OnColorChangeListener {
        fun onRelease()
        fun onColorChangeListener(color: Int)
        fun onPositionChangeListener(position: Int)
    }
}