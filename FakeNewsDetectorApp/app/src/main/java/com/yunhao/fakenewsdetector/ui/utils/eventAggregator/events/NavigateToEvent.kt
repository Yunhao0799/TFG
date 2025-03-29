package com.yunhao.fakenewsdetector.ui.utils.eventAggregator.events

import androidx.annotation.IdRes

data class NavigateToEvent(
    @IdRes val resId: Int
) : Event()