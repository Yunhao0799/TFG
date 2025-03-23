package com.yunhao.fakenewsdetector.ui.utils.eventAggregator

import com.yunhao.fakenewsdetector.ui.utils.eventAggregator.events.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventAggregator @Inject constructor() {
    private val _events = MutableSharedFlow<Event>(
        replay = 1,
        extraBufferCapacity = 64)
    val events = _events.asSharedFlow()

    private val scope = CoroutineScope(Dispatchers.Main.immediate)
    fun publish(event: Event){
        scope.launch {
            _events.emit(event)
        }
    }

    inline fun <reified T : Event> subscribe(
        scope: CoroutineScope,
        noinline handler: (T) -> Unit
    ): Job {
        return scope.launch {
            events.filterIsInstance<T>().collect{
                handler(it)
            }
        }
    }
}