package com.yunhao.fakenewsdetector.ui.utils.eventAggregator.events

import android.content.Context
import com.yunhao.fakenewsdetector.ui.utils.DialogButton

sealed class PopupEvent : Event()

data class ShowCustomDialogEvent(
    val context: Context,
    val title: String,
    val message: String,
    val buttons: List<DialogButton>,
    val isCancellable: Boolean = true,
) : PopupEvent()

data class ShowBusyDialogEvent(
    val context: Context,
    val title: String,
    val message: String,
    val isCancellable: Boolean = false,
) : PopupEvent()

sealed class HideCurrentDialogEvent : PopupEvent()