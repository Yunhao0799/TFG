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

@AndroidEntryPoint
class HomeFragment : FragmentBase<FragmentHomeBinding, ViewModelBase>() {
    override val viewModel: HomeViewModel by viewModels()

    private val messages = mutableListOf<ChatMessage>()
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

        adapter = ChatAdapter(messages)
        binding?.recyclerView?.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(requireContext())
        }

        setUpListeners()
    }


    override fun setUpListeners() {
        binding?.let { b ->
            b.submitButton?.setOnClickListener {
                val text = b.textInputEditText?.text.toString()
                if (!text.isNullOrBlank()) {
                    adapter.addMessage(ChatMessage(text, true))
                    b.recyclerView?.scrollToPosition(messages.size-1)
                    b.textInputEditText?.text?.clear()
                }

                // Fake a reply
                Handler(Looper.getMainLooper()).postDelayed({
                    adapter.addMessage(ChatMessage("Auto-reply: $text", false))
                    b.recyclerView.scrollToPosition(messages.size - 1)
                }, 1000)
            }
        }
    }
}