package com.yunhao.fakenewsdetector.ui.utils

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DialogsManager @Inject constructor(){
    fun showCustomDialog(
        context: Context,
        title: String,
        message: String,
        positiveButton: String = "OK",
        negativeButton: String? = null,
        onPositiveClick: (() -> Unit)? = null,
        onNegativeClick: (() -> Unit)? = null
    )
    {
        MaterialAlertDialogBuilder(context)
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
            .show()
            .setCancelable(false)
    }
}