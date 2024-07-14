package com.yunhao.fakenewsdetector.ui.viewmodel

import androidx.databinding.Bindable
import com.yunhao.fakenewsdetector.ui.viewmodel.common.ViewModelBase
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yunhao.fakenewsdetector.domain.services.LoginService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginService: LoginService
) : ViewModelBase() {
    var welcomeText: String = ""
        @Bindable get
        set(value){
            if (value != field){
                field = value
                notifyPropertyChanged(BR.welcomeText)
            }
        }

    init {
        welcomeText = "caracola"
    }


    fun login(username :String,
              password :String,
              onLogin: (Boolean) -> Unit) {

        viewModelScope.launch(Dispatchers.Main){
            val hasLoggedIn = loginService.login(username, password)
            onLogin?.invoke(hasLoggedIn)
        }
    }
}