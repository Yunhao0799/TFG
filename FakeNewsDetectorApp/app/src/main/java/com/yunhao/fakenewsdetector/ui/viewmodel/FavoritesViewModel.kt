package com.yunhao.fakenewsdetector.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.yunhao.fakenewsdetector.data.model.PredictionResponseDTO
import com.yunhao.fakenewsdetector.domain.mappers.toDomain
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
class FavoritesViewModel @Inject constructor(
    private val newsService: NewsService,
    private val eventAggregator: EventAggregator,
    private val predictionService: PredictionService,
) : ViewModelBase() {

    private val _news = MutableLiveData<List<ArticleUi>>(emptyList())
    val news: LiveData<List<ArticleUi>> = _news

    fun fetchNews(onFinishCallback: (() -> Unit)? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            eventAggregator.publish(PopupEvent.ShowBusyDialog())
            val result = newsService.getFavorites()
            Timber.d("$result")

            val gson = Gson()
            val newArticles = result?.articles?.map { article ->
                val prediction = article.prediction?.let {
                    val dto = gson.fromJson(it, PredictionResponseDTO::class.java)
                    predictionService.predictionToString(dto.toDomain())
                }

                ArticleUi(
                    id = article.id,
                    title = article.title,
                    description = article.description,
                    urlImage = article.imageUrl,
                    url = article.url,
                    publishedAt = article.publishedAt,
                    isFavorite = article.isFavorite,
                    predictionResult = prediction
                )
            }.orEmpty()

            _news.postValue(newArticles)

            eventAggregator.publish(PopupEvent.HideCurrentDialog)

            withContext(Dispatchers.Main) {
                onFinishCallback?.invoke()
            }
        }
    }

    fun toggleFavorite(articleUi: ArticleUi) {
        viewModelScope.launch(Dispatchers.IO) {
            newsService.toggleFavorite(articleUi.id, articleUi.isFavorite)
        }
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