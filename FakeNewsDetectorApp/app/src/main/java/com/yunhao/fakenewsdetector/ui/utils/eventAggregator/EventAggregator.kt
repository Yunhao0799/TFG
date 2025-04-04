package com.yunhao.fakenewsdetector.ui.utils.eventAggregator

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.yunhao.fakenewsdetector.ui.utils.eventAggregator.events.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventAggregator @Inject constructor() {

    private val _events = MutableSharedFlow<Event>(
        extraBufferCapacity = 64
    )
    val events = _events.asSharedFlow()

    suspend fun publish(event: Event,
                        useMainDispatcher: Boolean = false,) {
        _events.emit(event)
    }

    inline fun <reified T : Event> eventsOf(): Flow<T> {
        return events.filterIsInstance<T>()
    }
}

inline fun <reified T : Event> EventAggregator.subscribe(
    lifecycleOwner: LifecycleOwner,
    useMainDispatcher: Boolean = false,
    noinline handler: suspend (T) -> Unit
) {
    lifecycleOwner.lifecycleScope.launch {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            var flow = eventsOf<T>()
            if (useMainDispatcher) {
                flow = flow.flowOn(Dispatchers.Main)
            }
            flow.collect { event ->
                handler(event)
            }
        }
    }
}