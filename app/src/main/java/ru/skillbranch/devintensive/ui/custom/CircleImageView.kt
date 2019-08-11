package ru.skillbranch.devintensive.ui.custom

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.annotation.Dimension
import androidx.annotation.Dimension.DP
import androidx.annotation.DrawableRes
import ru.skillbranch.devintensive.R

class CircleImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    style: Int = 0
) : ImageView(context, attrs, style) {

    companion object {
        private const val DEFAULT_BORDER_COLOR = Color.WHITE
        private const val DEFAULT_BORDER_WIDTH = 2
    }

    private var borderColor = DEFAULT_BORDER_COLOR
    private var borderWidth = DEFAULT_BORDER_WIDTH
    private val graphicsMatrix = Matrix()
    private val bitmapPaint = Paint()
    private val borderPaint = Paint()
    private var mBitmap: Bitmap? = null
    private var bitmapShader: BitmapShader? = null
    private val drawableRect = RectF()
    private val borderRect = RectF()
    private var drawableRadius: Float = 0.0f
    private var borderRadius: Float = 0.0f
    private var bitmapWidth: Int = 0
    private var bitmapHeight: Int = 0
    private var ready: Boolean = false
    private var isPending: Boolean = false
    private var circleBackgroundColor = Color.BLACK
    private val circleBackgroundPaint = Paint()
    private var displayKey: String? = null

    init {
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView)
            borderWidth = a.getDimensionPixelSize(R.styleable.CircleImageView_cv_borderWidth, DEFAULT_BORDER_WIDTH)
            borderColor = a.getColor(R.styleable.CircleImageView_cv_borderColor, DEFAULT_BORDER_COLOR)

            a.recycle()
            setCircleColor(context.getColor(R.color.color_accent))
            init()
        }
    }

    private fun init() {
        ready = true
        if (isPending) {
            setup()
            isPending = false
        }
    }

    @Dimension(unit = DP)
    fun getBorderWidth(): Int {
        return borderWidth
    }

    fun setBorderWidth(@Dimension(unit = DP) borderWidth: Int) {
        if (borderWidth == this.borderWidth) {
            return
        }

        this.borderWidth = borderWidth
        setup()
    }

    fun getBorderColor(): Int {
        return borderColor
    }

    fun setBorderColor(hex: String) {
        val color = Color.parseColor(hex)
        //setBorderColor(color)
    }

    fun setBorderColor(@ColorRes borderColor: Int) {
        if (borderColor == this.borderColor) {
            return
        }

        this.borderColor = borderColor
        borderPaint.color = this.borderColor
        invalidate()
    }

    fun setDisplayText(text: String?) {
        displayKey = text
        setup()
    }

    fun getDisplayText() = displayKey

    fun setCircleColor(color: Int) {
        circleBackgroundColor = color
        circleBackgroundPaint.color = color
        setup()
    }

    override fun onDraw(canvas: Canvas) {

        if (displayKey!!.isNotEmpty()) {
            val mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
            mTextPaint.isAntiAlias = true
            mTextPaint.isFakeBoldText = true
            mTextPaint.setShadowLayer(6f, 0f, 0f, Color.BLACK)
            mTextPaint.style = Paint.Style.FILL
            mTextPaint.textAlign = Paint.Align.CENTER
            mTextPaint.color = Color.WHITE
            mTextPaint.textSize = 120f

            canvas.drawCircle(drawableRect.centerX(), drawableRect.centerY(), drawableRadius, circleBackgroundPaint)
            canvas.drawText(
                displayKey!!,
                0,
                displayKey!!.length,
                drawableRect.centerX(),
                drawableRect.centerY() - mTextPaint.ascent() / 2,
                mTextPaint
            )
        } else if (mBitmap != null) {
            canvas.drawCircle(drawableRect.centerX(), drawableRect.centerY(), drawableRadius, bitmapPaint)
        }

        if (borderWidth > 0) {
            canvas.drawCircle(borderRect.centerX(), borderRect.centerY(), borderRadius, borderPaint)
        }
    }

    private fun setup() {
        if (!ready) {
            isPending = true
            return
        }

        if (width == 0 && height == 0) {
            return
        }

        borderPaint.style = Paint.Style.STROKE
        borderPaint.isAntiAlias = true
        borderPaint.color = borderColor
        val borderWidth = borderWidth * resources.displayMetrics.density
        borderPaint.strokeWidth = borderWidth

        borderRect.set(calculateBounds())
        borderRadius =
            Math.min((borderRect.height() - this.borderWidth) / 2.0f, (borderRect.width() - this.borderWidth) / 2.0f)

        drawableRect.set(borderRect)
        drawableRadius = Math.min(drawableRect.height() / 2.0f, drawableRect.width() / 2.0f)

        if (mBitmap == null) {
            invalidate()
            return
        }

        bitmapShader = BitmapShader(mBitmap!!, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)

        bitmapPaint.isAntiAlias = true
        bitmapPaint.shader = bitmapShader

        bitmapHeight = mBitmap!!.height
        bitmapWidth = mBitmap!!.width

        updateShaderMatrix()
        invalidate()
    }

    private fun calculateBounds(): RectF {
        val availableWidth = width - paddingLeft - paddingRight
        val availableHeight = height - paddingTop - paddingBottom

        val sideLength = Math.min(availableWidth, availableHeight)

        val left = paddingLeft + (availableWidth - sideLength) / 2f
        val top = paddingTop + (availableHeight - sideLength) / 2f

        return RectF(left, top, left + sideLength, top + sideLength)
    }

    private fun updateShaderMatrix() {
        val scale: Float
        var dx = 0f
        var dy = 0f

        graphicsMatrix.set(null)

        if (bitmapWidth * drawableRect.height() > drawableRect.width() * bitmapHeight) {
            scale = drawableRect.height() / bitmapHeight.toFloat()
            dx = (drawableRect.width() - bitmapWidth * scale) * 0.5f
        } else {
            scale = drawableRect.width() / bitmapWidth.toFloat()
            dy = (drawableRect.height() - bitmapHeight * scale) * 0.5f
        }

        graphicsMatrix.setScale(scale, scale)
        graphicsMatrix.postTranslate((dx + 0.5f).toInt() + drawableRect.left, (dy + 0.5f).toInt() + drawableRect.top)

        bitmapShader!!.setLocalMatrix(graphicsMatrix)
    }

    private fun getBitmapFromDrawable(drawable: Drawable?): Bitmap? {
        if (drawable == null) {
            return null
        }

        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }

        try {
            val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            return bitmap
        } catch (exc: Exception) {
            throw exc
        }
    }

    private fun initializeBitmap() {
        mBitmap = getBitmapFromDrawable(drawable)
        setup()
    }

    override fun setImageBitmap(bm: Bitmap) {
        super.setImageBitmap(bm)
        initializeBitmap()
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        initializeBitmap()
    }

    override fun setImageResource(@DrawableRes resId: Int) {
        super.setImageResource(resId)
        initializeBitmap()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        setup()
    }

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        super.setPadding(left, top, right, bottom)
        setup()
    }

    override fun setPaddingRelative(start: Int, top: Int, end: Int, bottom: Int) {
        super.setPaddingRelative(start, top, end, bottom)
        setup()
    }
}