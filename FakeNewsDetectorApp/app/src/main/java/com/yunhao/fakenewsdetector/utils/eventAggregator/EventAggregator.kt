package com.yunhao.fakenewsdetector.utils.eventAggregator

import com.yunhao.fakenewsdetector.utils.eventAggregator.events.EventData
import kotlinx.coroutines.CompletionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.launch

class EventAggregator {
    private val _events = MutableSharedFlow<EventData>(extraBufferCapacity = 64)
    val events = _events.asSharedFlow()

    private val scope = CoroutineScope(Dispatchers.Main.immediate)
    fun publish(event: EventData){
        scope.launch {
            _events.emit(event)
        }
    }

    inline fun <reified T : EventData> subscribe(
        scope: CoroutineScope,
        noinline handler: (T) -> Unit
    ): Job {
        return scope.launch {
            events.filterIsInstance<T>().collect{
                handler(it) // Activate subscribers of the same class Type
            }
        }
    }
}