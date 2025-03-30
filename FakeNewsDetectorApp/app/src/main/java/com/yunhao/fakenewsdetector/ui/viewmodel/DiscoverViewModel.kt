package com.yunhao.fakenewsdetector.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.yunhao.fakenewsdetector.domain.services.NewsService
import com.yunhao.fakenewsdetector.ui.utils.eventAggregator.EventAggregator
import com.yunhao.fakenewsdetector.ui.utils.eventAggregator.events.PopupEvent
import com.yunhao.fakenewsdetector.ui.viewmodel.common.ViewModelBase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val newsService: NewsService,
    private val eventAggregator: EventAggregator
) : ViewModelBase() {

    fun fetchNews() {
        viewModelScope.launch(Dispatchers.IO) {
            eventAggregator.publish(PopupEvent.ShowBusyDialog())
            val result = newsService.getNews(endpoint="top-headlines", country = "us")
            Timber.d("$result")
            eventAggregator.publish(PopupEvent.HideCurrentDialog)
        }
    }
}