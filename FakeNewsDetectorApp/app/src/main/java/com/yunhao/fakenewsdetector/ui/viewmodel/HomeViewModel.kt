package com.yunhao.fakenewsdetector.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.yunhao.fakenewsdetector.domain.services.PredictionService
import com.yunhao.fakenewsdetector.ui.view.adapters.data.ChatMessage
import com.yunhao.fakenewsdetector.ui.viewmodel.common.ViewModelBase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val predictionService: PredictionService
) : ViewModelBase(){
    private val _messages = MutableLiveData<List<ChatMessage>>(emptyList())
    val messages: LiveData<List<ChatMessage>> = _messages

    // BINDING
    val messageInput = MutableLiveData<String>()

    private val _canSend = MutableLiveData(true)
    val canSend: LiveData<Boolean> = _canSend

    val isSendEnabled = MediatorLiveData<Boolean>().apply {
        addSource(messageInput) {
            value = checkCanSend()
        }
        addSource(_canSend) {
            value = checkCanSend()
        }
    }

    fun onSendClicked() {
        val text = messageInput.value?.trim()
        if (!text.isNullOrEmpty()) {
            _canSend.value = false
            val updatedList = _messages.value.orEmpty() + ChatMessage(text, true)
            _messages.value = updatedList
            messageInput.value = ""

            viewModelScope.launch(Dispatchers.IO) {
                val response = predictionService.predict(text)
                val replyList = _messages.value.orEmpty() + ChatMessage("Reply:\n $response", false)
                _messages.postValue(replyList)

                withContext(Dispatchers.Main){
                    _canSend.value = true
                }
            }
        }
    }

    private fun checkCanSend() : Boolean {
        return !messageInput.value.isNullOrBlank() && (_canSend.value == true)
    }
}