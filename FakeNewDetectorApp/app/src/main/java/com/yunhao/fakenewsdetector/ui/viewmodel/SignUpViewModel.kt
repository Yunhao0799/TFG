package com.yunhao.fakenewsdetector.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yunhao.fakenewsdetector.domain.services.SignUpService
import com.yunhao.fakenewsdetector.ui.viewmodel.common.ViewModelBase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpService: SignUpService
): ViewModelBase(){

    fun signUp(name: String,
               lastname: String,
               birthdate: String,
               email: String,
               password: String){
        viewModelScope.launch(Dispatchers.Main) {
            signUpService.signUp(name, lastname, birthdate, email, password)
        }
    }
}