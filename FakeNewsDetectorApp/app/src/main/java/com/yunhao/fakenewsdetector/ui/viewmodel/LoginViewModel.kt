package com.yunhao.fakenewsdetector.ui.viewmodel

import android.content.Context
import androidx.databinding.Bindable
import com.yunhao.fakenewsdetector.ui.viewmodel.common.ViewModelBase
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yunhao.fakenewsdetector.R
import com.yunhao.fakenewsdetector.domain.services.LoginService
import com.yunhao.fakenewsdetector.utils.UserHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import androidx.lifecycle.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginService: LoginService,
    @ApplicationContext private val context: Context
) : ViewModelBase() {

    // --- Properties ---
    var welcomeText: String = ""
        @Bindable get
        set(value){
            if (value != field){
                field = value
                notifyPropertyChanged(BR.welcomeText)
            }
        }

    // LiveData for email and password
    val email = MutableLiveData("")
    val password = MutableLiveData("")

    // LiveData for validation errors
    val emailError: LiveData<String?> = email.map {
        if (UserHelper.isValidEmail(it)) {
            null
        }
        else {
            context.getString(R.string.invalid_email)
        }
    }

    val passwordError: LiveData<String?> = password.map {
        if (it.length >= 6) {
            null
        }
        else {
            context.getString(R.string.invalid_pass)
        }
    }

    val isLoginEnabled = MediatorLiveData<Boolean>().apply {
        addSource(emailError) { checkValidation() }
        addSource(passwordError) { checkValidation() }
    }


    // --- Initialization ---
    init {
        welcomeText = "caracola"
    }


    // --- Methods ---
    fun login(onLogin: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.Main){
            val hasLoggedIn = loginService.login(email.value!!, password.value!!)
            onLogin?.invoke(hasLoggedIn)
        }
    }

    private fun checkValidation() {
        isLoginEnabled.value = (emailError.value == null && passwordError.value == null)
    }

}