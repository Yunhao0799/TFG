package com.yunhao.fakenewsdetector.ui.viewmodel

import androidx.databinding.Bindable
import com.yunhao.fakenewsdetector.ui.viewmodel.common.ViewModelBase
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginViewModel() : ViewModelBase() {

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


    fun testFun (){

        viewModelScope.launch(Dispatchers.Main){
            testSuspend{
                welcomeText = "it has been 10 seconds"
            }
        }

    }

    private suspend fun testSuspend(function: () -> Unit) {
        delay(1000 * 2)

        function.invoke()
    }

}