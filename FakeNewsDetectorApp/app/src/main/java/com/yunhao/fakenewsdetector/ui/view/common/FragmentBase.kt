package com.yunhao.fakenewsdetector.ui.view.common

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel

abstract class FragmentBase<T: ViewDataBinding, U: ViewModel> : Fragment() {
    protected var binding: T? = null
    protected abstract val viewModel: U?

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        binding?.lifecycleOwner = viewLifecycleOwner
        binding?.setVariable(BR.viewModel, viewModel)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpObservers()
        setUpListeners()
        setUpSubscribers()
    }

    abstract fun getLayoutId(): Int

    protected open fun setUpListeners() {

    }

    protected open fun setUpObservers() {

    }

    protected open fun setUpSubscribers() {

    }
}