package com.yunhao.fakenewsdetector.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yunhao.fakenewsdetector.ui.viewmodel.common.ViewModelBase
import kotlin.Result.Companion.success

class UserViewModel : ViewModelBase() {

    var isUserLoggedIn: MutableLiveData<Boolean> = MutableLiveData(false)

    fun login(username: String?, password: String?): Result<Boolean> {
        isUserLoggedIn.value = true

        return success(true)
    }
}