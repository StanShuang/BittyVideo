package com.stan.video.bittyvideo.view

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.stan.video.bittyvideo.R
import com.stan.video.bittyvideo.utils.TypeFaceUtil

/**
 *@Author Stan
 *@Description 带有自定义字体TextView。
 *@Date 2025/4/9 15:03
 */
class TypefaceTextView : AppCompatTextView {
    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        if (isInEditMode) return
        attrs?.let {
            val typeArray = context.obtainStyledAttributes(it, R.styleable.TypefaceTextView, 0, 0)
            val typefaceType = typeArray.getInt(R.styleable.TypefaceTextView_typeface, 0)
            typeface = Companion.getTypeface(typefaceType)
            typeArray.recycle()
        }
    }

    companion object {
        /**
         * 根据字体类型，获取自定义字体。
         */
        fun getTypeface(typefaceType: Int?): Typeface = when (typefaceType) {
            TypeFaceUtil.FZLL_TYPEFACE -> TypeFaceUtil.fzlLTypeface
            TypeFaceUtil.FZDB1_TYPEFACE -> TypeFaceUtil.fzdb1Typeface
            TypeFaceUtil.FUTURA_TYPEFACE -> TypeFaceUtil.futuraTypeface
            TypeFaceUtil.DIN_TYPEFACE -> TypeFaceUtil.dinTypeface
            TypeFaceUtil.LOBSTER_TYPEFACE -> TypeFaceUtil.lobsterTypeface
            else -> Typeface.DEFAULT
        }

    }
}