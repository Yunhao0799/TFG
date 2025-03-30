package com.yunhao.fakenewsdetector.ui.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.google.android.material.transition.MaterialFadeThrough
import com.yunhao.fakenewsdetector.R
import com.yunhao.fakenewsdetector.databinding.FragmentDiscoverBinding
import com.yunhao.fakenewsdetector.ui.view.common.FragmentBase
import com.yunhao.fakenewsdetector.ui.viewmodel.DiscoverViewModel
import com.yunhao.fakenewsdetector.ui.viewmodel.common.ViewModelBase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DiscoverFragment : FragmentBase<FragmentDiscoverBinding, ViewModelBase>() {

    override val viewModel: DiscoverViewModel by viewModels()

    override fun getLayoutId(): Int {
        return R.layout.fragment_discover
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialFadeThrough()
        exitTransition = MaterialFadeThrough()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fetchNews()
    }
}