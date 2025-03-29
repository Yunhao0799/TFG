package com.yunhao.fakenewsdetector.domain.services

interface ISignUpService {
    suspend fun signUp(name: String, lastname: String, birthdate: String, email: String, password: String): Pair<Boolean, String>
}