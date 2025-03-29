package com.yunhao.fakenewsdetector.ui.viewmodel

import android.content.Context
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.yunhao.fakenewsdetector.R
import com.yunhao.fakenewsdetector.domain.services.LoginService
import com.yunhao.fakenewsdetector.ui.utils.StringProvider
import com.yunhao.fakenewsdetector.ui.utils.eventAggregator.EventAggregator
import com.yunhao.fakenewsdetector.ui.utils.eventAggregator.events.NavigateToEvent
import com.yunhao.fakenewsdetector.ui.utils.eventAggregator.events.PopupEvent
import com.yunhao.fakenewsdetector.ui.viewmodel.common.ViewModelBase
import com.yunhao.fakenewsdetector.utils.UserHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginService: LoginService,
    @ApplicationContext private val context: Context,
    private val eventAggregator: EventAggregator,
    private val stringProvider: StringProvider
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

    // Flags to track if the user has interacted with the field
    val emailTouched = MutableLiveData(false)
    val passwordTouched = MutableLiveData(false)

    // LiveData for validation errors
    val emailError: LiveData<String?> = email.switchMap {
        MutableLiveData(
            if (it.isNotEmpty() && !UserHelper.isValidEmail(it)) {
                context.getString(R.string.invalid_email)
            }
            else {
                null
            }
        )
    }

    val passwordError: LiveData<String?> = password.switchMap {
        MutableLiveData(
            if (it.isNotEmpty() && it.length < 8) {
                context.getString(R.string.invalid_pass)
            }
            else {
                null
            }
        )
    }

    val isLoginEnabled = MediatorLiveData<Boolean>().apply {
        addSource(emailError) { checkValidation() }
        addSource(passwordError) { checkValidation() }
    }


    // --- Initialization ---
    init {
        welcomeText = "caracola"
//        dialogsManager.startListening()
    }


    // --- Methods ---
    fun login() {
        viewModelScope.launch(Dispatchers.IO){
            eventAggregator.publish(PopupEvent.ShowBusyDialog())

            val hasLoggedIn = loginService.login(email.value!!, password.value!!)

            eventAggregator.publish(PopupEvent.HideCurrentDialog)

            if (hasLoggedIn) {
                eventAggregator.publish(NavigateToEvent(R.id.action_LoginFragment_to_mainFragment))
            }
            else {
                eventAggregator.publish(PopupEvent.ShowCustomDialog(
                    stringProvider.get(R.string.error_title),
                    stringProvider.get(R.string.error_login),
                ))
            }
        }
    }

    private fun checkValidation() {
        isLoginEnabled.value = emailError.value == null &&
                passwordError.value == null &&
                !email.value.isNullOrBlank() &&
                !password.value.isNullOrBlank()
    }
}