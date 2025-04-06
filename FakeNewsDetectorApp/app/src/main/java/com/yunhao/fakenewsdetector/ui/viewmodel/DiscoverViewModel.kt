package com.yunhao.fakenewsdetector.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yunhao.fakenewsdetector.domain.services.NewsService
import com.yunhao.fakenewsdetector.ui.utils.eventAggregator.EventAggregator
import com.yunhao.fakenewsdetector.ui.utils.eventAggregator.events.PopupEvent
import com.yunhao.fakenewsdetector.ui.view.adapters.data.ArticleUi
import com.yunhao.fakenewsdetector.ui.view.adapters.data.ChatMessage
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

    private val _news = MutableLiveData<List<ArticleUi>>(emptyList())
    val news: LiveData<List<ArticleUi>> = _news

    fun fetchNews() {
        viewModelScope.launch(Dispatchers.IO) {
            eventAggregator.publish(PopupEvent.ShowBusyDialog())
            val result = newsService.getNews(endpoint="top-headlines", country = "us")
            Timber.d("$result")

            val newArticles = result?.articles?.map {
                ArticleUi(it.title, it.description, it.imageUrl, it.url, it.publishedAt)
            }.orEmpty()

            val updatedList = _news.value.orEmpty() + newArticles
            _news.postValue(updatedList)

            eventAggregator.publish(PopupEvent.HideCurrentDialog)
        }
    }
}