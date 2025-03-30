package com.yunhao.fakenewsdetector.ui.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialFadeThrough
import com.yunhao.fakenewsdetector.R
import com.yunhao.fakenewsdetector.databinding.FragmentHomeBinding
import com.yunhao.fakenewsdetector.ui.view.adapters.ChatAdapter
import com.yunhao.fakenewsdetector.ui.view.adapters.data.ChatMessage
import com.yunhao.fakenewsdetector.ui.view.common.FragmentBase
import com.yunhao.fakenewsdetector.ui.viewmodel.HomeViewModel
import com.yunhao.fakenewsdetector.ui.viewmodel.common.ViewModelBase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers

@AndroidEntryPoint
class HomeFragment : FragmentBase<FragmentHomeBinding, ViewModelBase>() {
    override val viewModel: HomeViewModel by viewModels()

    private lateinit var adapter: ChatAdapter

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialFadeThrough()
        exitTransition = MaterialFadeThrough()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ChatAdapter()
        binding?.recyclerView?.let {
            it.layoutManager = LinearLayoutManager(requireContext())
            it.adapter = adapter
        }

        setUpListeners()
        setUpObservers()
    }


    override fun setUpListeners() {
        binding?.let { b ->
            b.recyclerView.viewTreeObserver.addOnGlobalLayoutListener {
                val lastPosition = adapter.itemCount - 1
                b.recyclerView.postDelayed({
                    if (lastPosition >= 0){
                        b.recyclerView.smoothScrollToPosition(lastPosition)
                    }
                }, 100)
            }
        }
    }

    override fun setUpObservers() {
        viewModel.messages.observe(viewLifecycleOwner) { updatedMessages ->
            adapter.submitList(updatedMessages) {
                val lastPosition = updatedMessages.size - 1
                if (lastPosition >= 0) {
                    binding?.recyclerView?.postDelayed({
                        binding?.recyclerView?.scrollToPosition(lastPosition)
                    }, 100)
                }
            }
        }
    }
}