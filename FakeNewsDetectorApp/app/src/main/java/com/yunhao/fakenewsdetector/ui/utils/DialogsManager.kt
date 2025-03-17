package com.yunhao.fakenewsdetector.ui.utils

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.yunhao.fakenewsdetector.R
import com.yunhao.fakenewsdetector.ui.utils.eventAggregator.EventAggregator
import com.yunhao.fakenewsdetector.ui.utils.eventAggregator.events.HidePopupEvent
import com.yunhao.fakenewsdetector.ui.utils.eventAggregator.events.ShowPopupEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DialogsManager @Inject constructor(
    private val context: Context,
    private val eventAggregator: EventAggregator
){
    private val appContext = context.applicationContext
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private val mutex = Mutex()
    private var currentAlertDialog: AlertDialog? = null

    init {
        startListening() // ✅ Ensure event subscriptions start
    }


    fun startListening(){
        val a = 0
        scope.launch {
            eventAggregator.subscribe<ShowPopupEvent>(scope){
                test()
            }
        }

        scope.launch {
            eventAggregator.subscribe<HidePopupEvent>(scope){
                test2()
            }
        }
    }

    private fun test2() {
        val a = 0
    }

    private fun test() {
        val a = 0
    }

    fun destroy(){
        scope.cancel()
    }

    suspend fun showCustomDialog(
        context: Context,
        title: String,
        message: String,
        positiveButton: String = "OK",
        negativeButton: String? = null,
        onPositiveClick: (() -> Unit)? = null,
        onNegativeClick: (() -> Unit)? = null,
        isCancellable: Boolean = true
    )
    {
        mutex.withLock {
            if (currentAlertDialog?.isShowing == true) {
                currentAlertDialog?.hide()
            }

            currentAlertDialog = MaterialAlertDialogBuilder(context)
                .setTitle("Test title")
                .setMessage("Test m,essgae")
                .setNeutralButton("Neutral") { dialog, which ->
                    // Respond to neutral button press
                }
                .setNegativeButton("Negative ") { dialog, which ->
                    // Respond to negative button press
                }
                .setPositiveButton("Positive ") { dialog, which ->
                    // Respond to positive button press
                }
                .show().also {
                    it.setCancelable(isCancellable)
                    it.setOnDismissListener{
                        currentAlertDialog = null
                    }
                }
        }
    }

    suspend fun showBusyDialog(
        context: Context,
        title: String = "",
        message: String = "",
        isCancellable: Boolean = false
    )
    {
        mutex.withLock {
            if (currentAlertDialog?.isShowing == true) {
                currentAlertDialog?.hide()
            }

            val inflater = LayoutInflater.from(context)
            val dialogView = inflater.inflate(R.layout.busy_dialog, null)

            val currentAlertDialog = MaterialAlertDialogBuilder(context)
                .setTitle("Processing...")
                .setView(dialogView)
                .setCancelable(isCancellable)
                .show()
        }
    }
}