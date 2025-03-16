package com.yunhao.fakenewsdetector.ui.viewmodel

import android.content.Context
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.yunhao.fakenewsdetector.R
import com.yunhao.fakenewsdetector.domain.services.SignUpService
import com.yunhao.fakenewsdetector.ui.viewmodel.common.ViewModelBase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpService: SignUpService,
    @ApplicationContext private val context: Context
) : ViewModelBase() {

    // LiveData for fields
    val name = MutableLiveData("")
    val lastname = MutableLiveData("")
    val birthdate = MutableLiveData("")
    val email = MutableLiveData("")
    val password = MutableLiveData("")
    val confirmPassword = MutableLiveData("")

    // LiveData for validation errors
    val nameError: LiveData<String?> = name.map {
        if (it.isNotEmpty()) null else context.getString(R.string.invalid_name)
    }

    val lastnameError: LiveData<String?> = lastname.map {
        if (it.isNotEmpty()) null else context.getString(R.string.invalid_lastname)
    }

    val birthdateError: LiveData<String?> = birthdate.map {
        if (it.isNotEmpty()) null else context.getString(R.string.invalid_birthdate)
    }

    val emailError: LiveData<String?> = email.map {
        if (Patterns.EMAIL_ADDRESS.matcher(it).matches()) null else context.getString(R.string.invalid_email)
    }

    val passwordError: LiveData<String?> = password.map {
        if (it.length >= 6) null else context.getString(R.string.invalid_pass)
    }

    val confirmPasswordError: LiveData<String?> = confirmPassword.map {
        if (it == password.value) null else context.getString(R.string.password_mismatch)
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
        ).all { it == null }
    }

    fun signUp(onSuccessCallback: (Boolean) -> Unit) {
        if (isSignUpEnabled.value == true) {
            viewModelScope.launch(Dispatchers.IO) {
                val result = signUpService.signUp(
                    name.value ?: "",
                    lastname.value ?: "",
                    birthdate.value ?: "",
                    email.value ?: "",
                    password.value ?: ""
                )

                withContext(Dispatchers.Main) {
                    onSuccessCallback(result)
                }
            }
        }
    }
}
