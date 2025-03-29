package com.yunhao.fakenewsdetector.ui.utils

import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.yunhao.fakenewsdetector.R
import com.yunhao.fakenewsdetector.ui.utils.eventAggregator.EventAggregator
import com.yunhao.fakenewsdetector.ui.utils.eventAggregator.events.PopupEvent
import com.yunhao.fakenewsdetector.ui.utils.eventAggregator.subscribe
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

@ActivityScoped
class DialogsManager @Inject constructor(
    private val eventAggregator: EventAggregator
) {
    private var currentAlertDialog: AlertDialog? = null
    private val dialogMutex = Mutex()

    fun subscribeToEvents(activity: FragmentActivity, lifecycleOwner: LifecycleOwner) {
        eventAggregator.subscribe<PopupEvent.ShowCustomDialog>(lifecycleOwner) {
            showCustomDialog(
                activity,
                it.title,
                it.message,
                it.buttons,
                it.isCancellable,
            )
        }

        eventAggregator.subscribe<PopupEvent.ShowBusyDialog>(lifecycleOwner) {
            showBusyDialog(
                activity,
                it.title,
                it.message,
                it.isCancellable,
            )
        }

        eventAggregator.subscribe<PopupEvent.HideCurrentDialog>(lifecycleOwner) {
            hideCurrentDialog()
        }
    }

    suspend fun showCustomDialog(
        activity: FragmentActivity,
        title: String,
        message: String,
        buttons: List<DialogButton>? =
            listOf(
                DialogButton(
                    DialogButtonType.POSITIVE,
                    activity.getString(R.string.ok)
                ) {
                    hideCurrentDialog()
                }
            ),
        isCancellable: Boolean = true
    ) {
        dialogMutex.withLock {
            if (activity.isFinishing || activity.isDestroyed) return

            hideCurrentDialog()

            currentAlertDialog = MaterialAlertDialogBuilder(activity)
                .setTitle(title)
                .setMessage(message)
                .apply {
                    buttons?.let { applyButtons(*it.toTypedArray()) }
                }
                .show()
                .also {
                    it.setCancelable(isCancellable)
                    it.setOnDismissListener {
                        currentAlertDialog = null
                    }
                }
        }
    }

    suspend fun showBusyDialog(
        activity: FragmentActivity,
        title: String = "Processing...",
        message: String = "",
        isCancellable: Boolean = false
    ) {
        dialogMutex.withLock {
            if (activity.isFinishing || activity.isDestroyed) return

            hideCurrentDialog()

            val inflater = LayoutInflater.from(activity)
            val dialogView = inflater.inflate(R.layout.busy_dialog, null)

            currentAlertDialog = MaterialAlertDialogBuilder(activity)
                .setTitle(title)
                .setView(dialogView)
                .setCancelable(isCancellable)
                .show()
        }
    }

    fun hideCurrentDialog() {
        if (currentAlertDialog?.isShowing == true) {
            currentAlertDialog?.dismiss()
            currentAlertDialog = null
        }
    }

    suspend fun hideCurrentDialogSafely() {
        dialogMutex.withLock {
            hideCurrentDialog()
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