package com.mrcd.custom.view.provider

import com.mrcd.custom.view.provider.ContextHolder

fun screenWidth(): Int {
    val context = ContextHolder.getContext()
    return context.resources.displayMetrics.widthPixels
}

fun screenHeight(): Int {
    val context = ContextHolder.getContext()
    return context.resources.displayMetrics.heightPixels
}

fun dp2px(dp: Int): Int {
    val context = ContextHolder.getContext()
    val displayMetrics = context.resources.displayMetrics
    return (dp * displayMetrics.density + 0.5f).toInt()
}

fun px2dp(px: Int): Int {
    val context = ContextHolder.getContext()
    val displayMetrics = context.resources.displayMetrics
    return (px / displayMetrics.density + 0.5f).toInt()
}