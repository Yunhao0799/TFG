package com.yunhao.fakenewsdetector.ui.view.common

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.ViewModel

abstract class ActivityBase<T: ViewDataBinding, U: ViewModel> : AppCompatActivity() {
    protected var binding: T? = null
    protected abstract val viewModel: U?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, getLayoutId())
        binding?.lifecycleOwner = this
        binding?.setVariable(BR.viewModel, viewModel)
    }

    abstract fun getLayoutId(): Int
}