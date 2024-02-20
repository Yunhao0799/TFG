package com.yunhao.fakenewsdetector.ui.viewmodel

import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.viewModelScope
import com.yunhao.fakenewsdetector.ui.viewmodel.common.ViewModelBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class StartViewModel : ViewModelBase() {
    var testText: String = ""
        @Bindable get
        set(value){
            if (value != field){
                field = value
                notifyPropertyChanged(BR.testText)
            }
        }

    init {
        viewModelScope.launch(Dispatchers.Main){
            delay(1000 * 5)
            testText = "Juan Jose"
        }
    }
}