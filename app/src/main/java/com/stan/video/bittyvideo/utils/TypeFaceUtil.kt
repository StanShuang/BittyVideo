package com.stan.video.bittyvideo.utils

import android.graphics.Typeface
import com.stan.video.bittyvideo.app.MyApplication

/**
 *@Author Stan
 *@Description 自定义字体工具类。
 *@Date 2025/4/9 15:09
 */
object TypeFaceUtil {
    const val FZLL_TYPEFACE = 1

    const val FZDB1_TYPEFACE = 2

    const val FUTURA_TYPEFACE = 3

    const val DIN_TYPEFACE = 4

    const val LOBSTER_TYPEFACE = 5

    val fzlLTypeface: Typeface by lazy {
        Typeface.createFromAsset(
            MyApplication.context.assets,
            "fonts/FZLanTingHeiS-L-GB-Regular.TTF"
        )
    }

    val fzdb1Typeface: Typeface by lazy {
        Typeface.createFromAsset(
            MyApplication.context.assets,
            "fonts/FZLanTingHeiS-DB1-GB-Regular.TTF"
        )
    }

    val futuraTypeface: Typeface by lazy {
        Typeface.createFromAsset(MyApplication.context.assets, "fonts/Futura-CondensedMedium.ttf")
    }

    val dinTypeface: Typeface by lazy {
        Typeface.createFromAsset(MyApplication.context.assets, "fonts/DIN-Condensed-Bold.ttf")
    }

    val lobsterTypeface: Typeface by lazy {
        Typeface.createFromAsset(MyApplication.context.assets, "fonts/Lobster-1.4.otf")
    }
}