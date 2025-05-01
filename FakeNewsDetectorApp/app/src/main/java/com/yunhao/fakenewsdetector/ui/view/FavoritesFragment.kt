package com.yunhao.fakenewsdetector.ui.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialFadeThrough
import com.yunhao.fakenewsdetector.R
import com.yunhao.fakenewsdetector.databinding.FragmentFavoritesBinding
import com.yunhao.fakenewsdetector.ui.utils.LinkHelper.openUrl
import com.yunhao.fakenewsdetector.ui.view.adapters.NewsAdapter
import com.yunhao.fakenewsdetector.ui.view.common.FragmentBase
import com.yunhao.fakenewsdetector.ui.viewmodel.FavoritesViewModel
import com.yunhao.fakenewsdetector.ui.viewmodel.common.ViewModelBase
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class FavoritesFragment : FragmentBase<FragmentFavoritesBinding, ViewModelBase>() {

    override val viewModel: FavoritesViewModel by viewModels()

    private lateinit var adapter: NewsAdapter

    override fun getLayoutId(): Int {
        return R.layout.fragment_favorites
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