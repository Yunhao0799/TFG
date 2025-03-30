package com.yunhao.fakenewsdetector.domain.services

import com.yunhao.fakenewsdetector.domain.model.GetNewsResult

interface INewsService{
    suspend fun getNews(query: String,
                        language: String,
                        country: String,
                        category: String,
                        endpoint: String) : GetNewsResult?
}