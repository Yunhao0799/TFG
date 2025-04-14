package com.yunhao.fakenewsdetector.ui.view

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialFadeThrough
import com.yunhao.fakenewsdetector.R
import com.yunhao.fakenewsdetector.databinding.FragmentHomeBinding
import com.yunhao.fakenewsdetector.databinding.PopupProfileMenuBinding
import com.yunhao.fakenewsdetector.ui.view.adapters.ChatAdapter
import com.yunhao.fakenewsdetector.ui.view.common.FragmentBase
import com.yunhao.fakenewsdetector.ui.viewmodel.HomeViewModel
import com.yunhao.fakenewsdetector.ui.viewmodel.common.ViewModelBase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : FragmentBase<FragmentHomeBinding, ViewModelBase>() {
    override val viewModel: HomeViewModel by viewModels()

    private lateinit var adapter: ChatAdapter

    private val isProfilePopupOpen = MutableLiveData(false)
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
        isProfilePopupOpen.observe(viewLifecycleOwner) {
            (requireActivity() as StartActivity).isPopupOpen.postValue(it)
        }

        binding?.let { b ->
            b.recyclerView.viewTreeObserver.addOnGlobalLayoutListener {
                val lastPosition = adapter.itemCount - 1
                b.recyclerView.postDelayed({
                    if (lastPosition >= 0){
                        b.recyclerView.smoothScrollToPosition(lastPosition)
                    }
                }, 100)
            }

            b.appBar.let {
                it.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.profile -> {

                            openProfilePopup(it as View)
                            true
                        }

                        else -> false
                    }
                }
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

    fun openProfilePopup(anchorView: View) {
        val binding = PopupProfileMenuBinding.inflate(LayoutInflater.from(context), null, false)

        val popupWindow = PopupWindow(
            binding.root,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        ).apply {
            isOutsideTouchable = true
            elevation = 20f
            // setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), android.R.color.transparent))
            setOnDismissListener { isProfilePopupOpen.postValue(false) }
        }

        binding.apply {
            logoutText.setOnClickListener {
                popupWindow.dismiss()
            }

            logoutClickableArea.setOnClickListener {
                popupWindow.dismiss()
            }
        }

        // Support RTL layout
        val isRtl = ViewCompat.getLayoutDirection(anchorView) == ViewCompat.LAYOUT_DIRECTION_RTL
        val offsetX = if (isRtl) 200 else -200

        popupWindow.showAsDropDown(anchorView, offsetX, 10, Gravity.END)
        isProfilePopupOpen.postValue(true)
    }
}