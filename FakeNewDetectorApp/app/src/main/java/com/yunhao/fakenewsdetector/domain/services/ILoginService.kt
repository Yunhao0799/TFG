package com.yunhao.fakenewsdetector.domain.services

sealed interface ILoginService{
    fun login(username: String, password: String): Boolean
}