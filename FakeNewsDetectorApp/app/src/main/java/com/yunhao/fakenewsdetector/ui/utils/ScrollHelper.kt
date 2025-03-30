package com.yunhao.fakenewsdetector.ui.utils

import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ScrollView
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import com.yunhao.fakenewsdetector.ui.utils.ScrollHelper.scrollIntoViewOnFocus
import timber.log.Timber
import kotlin.math.roundToInt

object ScrollHelper {

    fun TextInputLayout.scrollIntoViewOnFocus(scrollView: ScrollView, delayMillis: Long = 150) {
        this.editText?.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                scrollView.postDelayed({
                    val rect = Rect()
                    v.getGlobalVisibleRect(rect)
                    scrollView.smoothScrollTo(0, rect.top - scrollView.height / 2)
                }, delayMillis)
            }
        }
    }

    fun ScrollView.scrollFocusedViewOnKeyboardOpen(rootView: View) {
        rootView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            private var wasKeyboardOpen = false

            override fun onGlobalLayout() {
                val rect = Rect()
                rootView.getWindowVisibleDisplayFrame(rect)
                val screenHeight = rootView.rootView.height
                val keypadHeight = screenHeight - rect.bottom
                val isKeyboardOpen = keypadHeight > screenHeight * 0.15

                if (isKeyboardOpen && !wasKeyboardOpen) {
                    wasKeyboardOpen = true
                    val focusedView = rootView.findFocus()
                    focusedView?.let { view ->
                        postDelayed({
                            val viewRect = Rect()
                            view.getGlobalVisibleRect(viewRect)
                            smoothScrollTo(0, viewRect.top - height / 2)
                        }, 100) // slight delay to wait for resize
                    }
                } else if (!isKeyboardOpen && wasKeyboardOpen) {
                    wasKeyboardOpen = false
                }
            }
        })
    }
}

@BindingAdapter("autoScrollOnFocus")
fun TextInputLayout.setAutoScrollOnFocus(scrollView: ScrollView?) {
    if (scrollView != null) {
        this.editText?.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                scrollView.postDelayed({
                    val rect = Rect()
                    v.getGlobalVisibleRect(rect)
                    val visibleFrame = Rect()
                    scrollView.rootView.getWindowVisibleDisplayFrame(visibleFrame)
                    val availableHeight = visibleFrame.height()
                    val targetScrollY = this.y.roundToInt() - (availableHeight / 3)
                    scrollView.smoothScrollTo(0, targetScrollY)
                }, 150)
            }
        }
    }
}