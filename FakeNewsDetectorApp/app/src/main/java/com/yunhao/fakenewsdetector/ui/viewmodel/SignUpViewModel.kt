package com.yunhao.fakenewsdetector.ui.viewmodel

import android.content.Context
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.yunhao.fakenewsdetector.R
import com.yunhao.fakenewsdetector.domain.services.SignUpService
import com.yunhao.fakenewsdetector.ui.utils.DialogsManager
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
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpService: SignUpService,
    @ApplicationContext private val context: Context,
    private val eventAggregator: EventAggregator,
    private val stringProvider: StringProvider
) : ViewModelBase() {

    // LiveData for fields
    val name = MutableLiveData("")
    val lastname = MutableLiveData("")
    val birthdate = MutableLiveData("")
    val email = MutableLiveData("")
    val password = MutableLiveData("")
    val confirmPassword = MutableLiveData("")

    // LiveData to indicate field has been interacted
    val nameTouched = MutableLiveData(false)
    val lastNameTouched = MutableLiveData(false)

    // LiveData for validation errors
    val nameError: LiveData<String?> = name.switchMap {
        MutableLiveData(
            if (true == nameTouched.value && it.isBlank()) {
                context.getString(R.string.invalid_name)
            }
            else {
                null
            }
        )
    }

    val lastnameError: LiveData<String?> = lastname.switchMap {
        MutableLiveData(
            if (true == lastNameTouched.value && it.isBlank()) {
                context.getString(R.string.invalid_name)
            }
            else {
                null
            }
        )
    }

    val birthdateError: LiveData<String?> = birthdate.switchMap {
        MutableLiveData(
            if (it.length < 2) {
                context.getString(R.string.invalid_birthdate)
            }
            else {
                null
            }
        )
    }

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

    val confirmPasswordError: LiveData<String?> = confirmPassword.switchMap {
        MutableLiveData(
            if (it.isNotEmpty() && it != password.value) {
                context.getString(R.string.password_mismatch)
            }
            else {
                null
            }
        )
    }

    // LiveData for enabling the sign-up button
    val isSignUpEnabled = MediatorLiveData<Boolean>().apply {
        addSource(nameError) { checkValidation() }
        addSource(lastnameError) { checkValidation() }
        addSource(birthdateError) { checkValidation() }
        addSource(emailError) { checkValidation() }
        addSource(passwordError) { checkValidation() }
        addSource(confirmPasswordError) { checkValidation() }
    }

    private fun checkValidation() {
        isSignUpEnabled.value = listOf(
                nameError.value,
                lastnameError.value,
                birthdateError.value,
                emailError.value,
                passwordError.value,
                confirmPasswordError.value
            ).all { it == null } and
                listOf(
                    name.value,
                    lastname.value,
                    birthdate.value,
                    email.value,
                    password.value,
                    confirmPassword.value
                    ).all { !it.isNullOrBlank()}
    }

    fun signUp() {
        if (isSignUpEnabled.value == true) {
            viewModelScope.launch(Dispatchers.IO) {
                eventAggregator.publish(PopupEvent.ShowBusyDialog())

                val result = signUpService.signUp(
                    name.value ?: "",
                    lastname.value ?: "",
                    birthdate.value ?: "",
                    email.value ?: "",
                    password.value ?: ""
                )

                eventAggregator.publish(PopupEvent.HideCurrentDialog)

                if (result.first) {
                    eventAggregator.publish(NavigateToEvent(R.id.action_signUpFragment_to_LoginFragment))
                }
                else {
                    eventAggregator.publish(PopupEvent.ShowCustomDialog(
                        stringProvider.get(R.string.error_title),
                        stringProvider.get(R.string.error_signup)
                                + "\n" + result.second
                    ))
                }
            }
        }
    }
}
