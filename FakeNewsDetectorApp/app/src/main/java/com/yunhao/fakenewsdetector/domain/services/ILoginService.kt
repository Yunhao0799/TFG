package com.yunhao.fakenewsdetector.domain.services

sealed interface ILoginService{
    suspend fun login(username: String, password: String): Boolean
}