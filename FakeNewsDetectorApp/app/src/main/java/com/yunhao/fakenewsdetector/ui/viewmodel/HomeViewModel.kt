package com.yunhao.fakenewsdetector.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.yunhao.fakenewsdetector.ui.view.adapters.data.ChatMessage
import com.yunhao.fakenewsdetector.ui.viewmodel.common.ViewModelBase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeViewModel : ViewModelBase(){
    private val _messages = MutableLiveData<List<ChatMessage>>(emptyList())
    val messages: LiveData<List<ChatMessage>> = _messages

    // BINDING
    val messageInput = MutableLiveData<String>()

    val isSendEnabled: LiveData<Boolean> = messageInput.map {
        !it.isNullOrBlank()
    }

    fun onSendClicked() {
        val text = messageInput.value?.trim()
        if (!text.isNullOrEmpty()) {
            val updatedList = _messages.value.orEmpty() + ChatMessage(text, true)
            _messages.value = updatedList
            messageInput.value = ""

            // Fake reply
            viewModelScope.launch {
                delay(1000)
                val replyList = _messages.value.orEmpty() + ChatMessage("Auto-reply: $text", false)
                _messages.value = replyList
            }
        }
    }
}