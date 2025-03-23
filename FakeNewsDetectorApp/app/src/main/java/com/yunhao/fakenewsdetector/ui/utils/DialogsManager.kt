package com.yunhao.fakenewsdetector.ui.utils

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.yunhao.fakenewsdetector.R
import com.yunhao.fakenewsdetector.ui.utils.eventAggregator.EventAggregator
import com.yunhao.fakenewsdetector.ui.utils.eventAggregator.events.HideCurrentDialogEvent
import com.yunhao.fakenewsdetector.ui.utils.eventAggregator.events.ShowBusyDialogEvent
import com.yunhao.fakenewsdetector.ui.utils.eventAggregator.events.ShowCustomDialogEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DialogsManager @Inject constructor(
    private val eventAggregator: EventAggregator
){
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var currentAlertDialog: AlertDialog? = null

    init {
        startListening()
    }

    fun startListening(){
        scope.launch {
            eventAggregator.subscribe<ShowCustomDialogEvent>(scope){
                showCustomDialog(
                    it.context,
                    it.title,
                    it.message,
                    it.buttons,
                    it.isCancellable,
                )
            }
        }

        scope.launch {
            eventAggregator.subscribe<ShowBusyDialogEvent>(scope){
                showBusyDialog(
                    it.context,
                    it.title,
                    it.message,
                    it.isCancellable,
                )
            }
        }

        scope.launch {
            eventAggregator.subscribe<HideCurrentDialogEvent>(scope){
                hideCurrentDialog()
            }
        }
    }

    fun stopListening(){
        scope.cancel()
    }

    fun showCustomDialog(
        context: Context,
        title: String,
        message: String,
        buttons: List<DialogButton>? =
            listOf(
                DialogButton(
                    DialogButtonType.POSITIVE,
                    context.getString(R.string.ok)) {
                    hideCurrentDialog()
                }
            ),
        isCancellable: Boolean = true
    )
    {
        hideCurrentDialog()

        currentAlertDialog = MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(message)
            .apply{
                buttons?.let{
                    applyButtons(*buttons.toTypedArray())
                }
            }
            .show().also {
                it.setCancelable(isCancellable)
                it.setOnDismissListener{
                    currentAlertDialog = null
                }
            }
    }

    fun showBusyDialog(
        context: Context,
        title: String = "",
        message: String = "",
        isCancellable: Boolean = false
    )
    {
        hideCurrentDialog()

        val inflater = LayoutInflater.from(context)
        val dialogView = inflater.inflate(R.layout.busy_dialog, null)

        currentAlertDialog = MaterialAlertDialogBuilder(context)
            .setTitle("Processing...")
            .setView(dialogView)
            .setCancelable(isCancellable)
            .show()
    }

    fun hideCurrentDialog() {
        if (currentAlertDialog?.isShowing == true) {
            currentAlertDialog?.hide()
            currentAlertDialog?.dismiss()
            currentAlertDialog = null
        }
    }
}

// Extension method to handle the button types
fun MaterialAlertDialogBuilder.applyButtons(vararg buttons: DialogButton): MaterialAlertDialogBuilder {
    buttons.forEach { button ->
        when (button.type) {
            DialogButtonType.POSITIVE -> setPositiveButton(button.text) { _, _ -> button.onClick?.invoke() }
            DialogButtonType.NEGATIVE -> setNegativeButton(button.text) { _, _ -> button.onClick?.invoke() }
            DialogButtonType.NEUTRAL -> setNeutralButton(button.text) { _, _ -> button.onClick?.invoke() }
        }
    }
    return this
}