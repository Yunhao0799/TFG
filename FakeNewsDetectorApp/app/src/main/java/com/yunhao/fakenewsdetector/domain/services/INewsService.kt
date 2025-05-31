package com.yunhao.fakenewsdetector.domain.services

import com.yunhao.fakenewsdetector.domain.model.GetNewsResult

interface INewsService{
    suspend fun getNews(query: String? = null,
                        language: String? = null,
                        country: String? = null,
                        category: String? = null,
                        endpoint: String? = null) : GetNewsResult?

    suspend fun toggleFavorite(articleId: Int) : Boolean
}