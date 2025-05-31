package com.yunhao.fakenewsdetector.ui.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialFadeThrough
import com.yunhao.fakenewsdetector.R
import com.yunhao.fakenewsdetector.databinding.FragmentDiscoverBinding
import com.yunhao.fakenewsdetector.ui.utils.LinkHelper.openUrl
import com.yunhao.fakenewsdetector.ui.view.adapters.NewsAdapter
import com.yunhao.fakenewsdetector.ui.view.common.FragmentBase
import com.yunhao.fakenewsdetector.ui.viewmodel.DiscoverViewModel
import com.yunhao.fakenewsdetector.ui.viewmodel.common.ViewModelBase
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class DiscoverFragment : FragmentBase<FragmentDiscoverBinding, ViewModelBase>() {

    override val viewModel: DiscoverViewModel by viewModels()

    private lateinit var adapter: NewsAdapter

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

        adapter = NewsAdapter(
            {
                it.url?.let { url -> requireContext().openUrl(url) }
            },
            {
                Timber.d("On Like")
                viewModel.toggleFavorite(it)
            },
            {
                Timber.d("On prediction callback")
                viewModel.predictNew(it)
                Timber.d("End3")
            }
        )
        binding?.let { b ->
            b.recyclerView.let {
                it.layoutManager = LinearLayoutManager(requireContext())
                it.adapter = adapter
            }
        }

        setUpObservers()
        setUpListeners()
        viewModel.fetchNews()
    }

    override fun setUpObservers() {
        super.setUpObservers()
        viewModel.news.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    override fun setUpListeners() {
        super.setUpListeners()
        binding?.let { b ->
            b.swipeRefreshLayout.let {
                it.setOnRefreshListener {
                    viewModel.fetchNews {
                        it.isRefreshing = false
                    }
                }
            }
        }
    }
}