package son.ysy.custom.tag

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.annotation.CheckResult
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import java.lang.Math.toRadians
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.*

class TagTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    sealed class Position(internal val value: Int) {
        object TopStart : Position(0)
        object TopEnd : Position(1)
        object BottomEnd : Position(2)
        object BottomStart : Position(3)
    }

    private val tagPaint = Paint()

    private val textPaint = Paint()

    private val contentBounds = RectF()

    private val tagRect = Rect()

    private val textRect: Rect = Rect()

    private var tagRotate: Float = 0f

    private var textStartPoint = Point(0, 0)

    private val dirty = AtomicBoolean(true)

    private var tagSizeRatio: Float

    private var tagStartRatio: Float

    private var position: Int

    @ColorInt
    private var tagColor: Int = Color.RED

    @ColorInt
    private var tagGradientStartColor: Int? = null

    @ColorInt
    private var tagGradientEndColor: Int? = null
    private var tagGradientAngle: Double? = null

    private var textRatio: Float

    private var textRotate: Float

    private var text: String

    @ColorInt
    private var textColor: Int

    init {
        context.obtainStyledAttributes(attrs, R.styleable.TagTextView).apply {

            tagSizeRatio = getFloat(R.styleable.TagTextView_ttv_tagSizeRatio, 0.35f)
            tagStartRatio = getFloat(R.styleable.TagTextView_ttv_tagStartRatio, 0f)
            position = getInt(R.styleable.TagTextView_ttv_tagPosition, 0)

            textRotate = getFloat(R.styleable.TagTextView_ttv_textRotate, 0f)
            text = getString(R.styleable.TagTextView_ttv_text) ?: ""
            textColor = getColor(R.styleable.TagTextView_ttv_textColor, Color.WHITE)
            textRatio = getFloat(R.styleable.TagTextView_ttv_textRatio, 0.6f)
            val hasGradientStartColor = hasValue(R.styleable.TagTextView_ttv_tagGradientStartColor)
            val hasGradientEndColor = hasValue(R.styleable.TagTextView_ttv_tagGradientEndColor)
            if (hasGradientStartColor && hasGradientEndColor) {
                tagGradientStartColor = getColor(
                    R.styleable.TagTextView_ttv_tagGradientStartColor,
                    Color.RED
                )
                tagGradientEndColor = getColor(
                    R.styleable.TagTextView_ttv_tagGradientEndColor,
                    Color.RED
                )
                tagGradientAngle = getFloat(
                    R.styleable.TagTextView_ttv_tagGradientAngle,
                    0f
                ).toDouble()
            } else {
                tagColor = getColor(R.styleable.TagTextView_ttv_tagColor, Color.RED)
            }

        }.recycle()

        tagPaint.strokeWidth = 0f
        tagPaint.style = Paint.Style.FILL
        textPaint.strokeWidth = 0f
        textPaint.style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (dirty.get()) {
            buildComponents(canvas)
            dirty.set(false)
        }

        canvas.saveLayer(contentBounds, tagPaint)

        canvas.translate(contentBounds.centerX(), contentBounds.centerY())
        canvas.rotate(tagRotate)

        canvas.drawRect(tagRect, tagPaint)

        canvas.translate(tagRect.centerX() * 1f, 0f)
        canvas.rotate(90f)

        canvas.rotate(textRotate)

        canvas.drawText(
            text,
            0,
            text.length,
            textStartPoint.x * 1f,
            textStartPoint.y * 1f,
            textPaint
        )

        canvas.restore()
    }

    private fun buildComponents(canvas: Canvas) {

        val realSize = min(canvas.width, canvas.height)

        contentBounds.set(
            (canvas.width - realSize) / 2f,
            (canvas.height - realSize) / 2f,
            (canvas.width + realSize) / 2f,
            (canvas.height + realSize) / 2f
        )

        val bgHeight = sqrt((realSize * tagSizeRatio).pow(2) / 2)

        val startSize = sqrt((realSize * tagStartRatio).pow(2) / 2).toInt()

        textPaint.textSize = bgHeight * textRatio
        textPaint.color = textColor

        tagRotate = when (position) {
            0 -> 180f + 45f
            1 -> 270f + 45f
            2 -> 45f
            else -> 90f + 45f
        }

        val contentSize = sqrt(realSize * realSize * 2f)
            .toInt()

        tagRect.set(
            contentSize / 2 - startSize,
            contentSize / -2,
            contentSize / 2 - startSize - bgHeight.toInt(),
            contentSize / 2
        )
        val tagGradientStartColor = tagGradientStartColor
        val tagGradientEndColor = tagGradientEndColor
        val tagGradientAngle = tagGradientAngle ?: 0.0
        if (tagGradientStartColor != null && tagGradientEndColor != null) {
            val gradientSize = sqrt(
                tagRect.width().toFloat().pow(2) +
                        tagRect.height().toFloat().pow(2)
            )
            val radians = toRadians(tagGradientAngle)
            val x = (cos(radians) / 2f * gradientSize).toFloat()
            val y = (sin(radians) / 2f * gradientSize).toFloat()
            val rectF = RectF(
                tagRect.centerX() + x,
                tagRect.centerY() + y,
                tagRect.centerX() - x,
                tagRect.centerY() - y,
            )

            tagPaint.shader = LinearGradient(
                tagRect.centerX() + x,
                tagRect.centerY() + y,
                tagRect.centerX() - x,
                tagRect.centerY() - y,
                tagGradientStartColor,
                tagGradientEndColor,
                Shader.TileMode.CLAMP
            )
        } else {
            tagPaint.color = tagColor
        }

        textPaint.getTextBounds(text, 0, text.length, textRect)

        textStartPoint.set(textRect.width() / -2, (textRect.height() - textRect.bottom) / 2)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        val size = min(width, height)
        setMeasuredDimension(size, size)
    }

    private fun setMemberDirty(): TagTextView {
        dirty.set(true)
        return this
    }

    /**
     * 设置角标宽度:控件的宽
     * 范围(0,1)
     */
    @CheckResult(suggest = "end with rebuild()")
    fun setTagSizeRatio(
        @FloatRange(from = 0.0, to = 1.0, fromInclusive = false, toInclusive = false)
        ratio: Float
    ): TagTextView {
        this.tagSizeRatio = ratio
        return setMemberDirty()
    }

    /**
     * 设置角标起始位置比例
     * 范围[0,1)
     */
    @CheckResult(suggest = "end with rebuild()")
    fun setTagStartRatio(
        @FloatRange(from = 0.0, to = 1.0, fromInclusive = true, toInclusive = false)
        ratio: Float
    ): TagTextView {
        this.tagStartRatio = ratio
        return setMemberDirty()
    }

    /**
     * 设置角标颜色
     */
    @CheckResult(suggest = "end with rebuild()")
    fun setTagColor(@ColorInt color: Int): TagTextView {
        this.tagColor = color
        this.tagGradientStartColor = null
        this.tagGradientEndColor = null
        return setMemberDirty()
    }

    /**
     * 设置角标渐变颜色
     */
    @CheckResult(suggest = "end with rebuild()")
    fun setTagGradientColor(
        @ColorInt startColor: Int,
        @ColorInt endColor: Int,
        angle: Double = 0.0
    ): TagTextView {
        this.tagGradientStartColor = startColor
        this.tagGradientEndColor = endColor
        this.tagGradientAngle = angle
        return setMemberDirty()
    }


    /**
     * 设置角标位置
     */
    @CheckResult(suggest = "end with rebuild()")
    fun setPosition(position: Position): TagTextView {
        this.position = position.value
        return setMemberDirty()
    }

    /**
     * 设置字体颜色
     */
    @CheckResult(suggest = "end with rebuild()")
    fun setTextColor(@ColorInt color: Int): TagTextView {
        this.textColor = color
        return setMemberDirty()
    }

    /**
     * 设置文字占角标比例
     * 范围(0,1)
     */
    @CheckResult(suggest = "end with rebuild()")
    fun setTextSizeRatio(
        @FloatRange(from = 0.0, to = 1.0, fromInclusive = false, toInclusive = false)
        ratio: Float
    ): TagTextView {
        this.textRatio = ratio
        return setMemberDirty()
    }

    /**
     * 设置文字旋转角度,旋转中心为角标中心
     */
    @CheckResult(suggest = "end with rebuild()")
    fun setTextRotate(rotate: Float): TagTextView {
        this.textRotate = rotate
        return setMemberDirty()
    }

    /**
     * 设置文字
     */
    @CheckResult(suggest = "end with rebuild()")
    fun setText(text: String): TagTextView {
        this.text = text
        return setMemberDirty()
    }

    fun rebuild() {
        postInvalidate()
    }
}