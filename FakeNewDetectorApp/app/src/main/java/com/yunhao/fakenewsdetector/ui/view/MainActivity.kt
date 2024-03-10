package com.yunhao.fakenewsdetector.ui.view

import android.os.Bundle
import com.yunhao.fakenewsdetector.R
import com.yunhao.fakenewsdetector.databinding.ActivityMainBinding
import com.yunhao.fakenewsdetector.ui.view.common.ActivityBase
import com.yunhao.fakenewsdetector.ui.viewmodel.MainViewModel
import com.yunhao.fakenewsdetector.ui.viewmodel.common.ViewModelBase
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ActivityBase<ActivityMainBinding, ViewModelBase>() {

    override val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }
}