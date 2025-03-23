package com.yunhao.fakenewsdetector.ui.utils

enum class DialogButtonType {
    POSITIVE,
    NEGATIVE,
    NEUTRAL
}

data class DialogButton(
    val type: DialogButtonType,
    val text: String,
    val onClick: (() -> Unit)? = null
)
