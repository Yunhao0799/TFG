package com.yunhao.fakenewsdetector.ui.utils.eventAggregator.events

sealed class PopupEvent : Event()

data class ShowPopupEvent(
    val message: String
) : PopupEvent()

class HidePopupEvent : PopupEvent()