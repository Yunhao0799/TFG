package com.yunhao.fakenewsdetector.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yunhao.fakenewsdetector.domain.services.NewsService
import com.yunhao.fakenewsdetector.domain.services.PredictionService
import com.yunhao.fakenewsdetector.ui.utils.eventAggregator.EventAggregator
import com.yunhao.fakenewsdetector.ui.utils.eventAggregator.events.PopupEvent
import com.yunhao.fakenewsdetector.ui.view.adapters.data.ArticleUi
import com.yunhao.fakenewsdetector.ui.viewmodel.common.ViewModelBase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val newsService: NewsService,
    private val eventAggregator: EventAggregator,
    private val predictionService: PredictionService,
) : ViewModelBase() {

    private val _news = MutableLiveData<List<ArticleUi>>(emptyList())
    val news: LiveData<List<ArticleUi>> = _news

    fun fetchNews(onFinishCallback: (() -> Unit)? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            eventAggregator.publish(PopupEvent.ShowBusyDialog())
            val result = newsService.getNews(endpoint="top-headlines", country = "us")
            Timber.d("$result")

            val newArticles = result?.articles?.map {
                ArticleUi(it.id, it.title, it.description, it.imageUrl, it.url,
                    it.publishedAt, it.isFavorite, null)
            }.orEmpty()

            _news.postValue(newArticles)

            eventAggregator.publish(PopupEvent.HideCurrentDialog)

            withContext(Dispatchers.Main) {
                onFinishCallback?.invoke()
            }
        }
    }

    fun predictNew(articleUi: ArticleUi) {
        viewModelScope.launch(Dispatchers.IO) {
            // delay(10000)
            val response = predictionService.predict(articleUi.title, articleUi.id)
            var predictionResult: String? = null
            if (null != response) {
                predictionResult = "This is probably " +
                        if (response.isFake) {
                            "fake with a " + String.format("%.2f", response.isFakeConfidence) + "% confidence"
                        } else {
                            "real with a " + String.format("%.2f", response.isRealConfidence)  + "% confidence"
                        }
            }

            withContext(Dispatchers.Main) {
                updateArticle(articleUi.copy(predictionResult = predictionResult, isPredicting = false))
            }
        }
    }

    fun likeArticle(articleUi: ArticleUi) {

    }

    private fun updateArticle(updatedArticle: ArticleUi) {
        _news.value = _news.value?.map {
            if (it.url == updatedArticle.url) {
                updatedArticle
            }
            else {
                it
            }
        }
    }
}