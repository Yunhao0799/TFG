package com.yunhao.fakenewsdetector.ui.view

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.yunhao.fakenewsdetector.R
import com.yunhao.fakenewsdetector.databinding.FragmentLoginBinding
import com.yunhao.fakenewsdetector.ui.utils.DialogsManager
import com.yunhao.fakenewsdetector.ui.utils.eventAggregator.EventAggregator
import com.yunhao.fakenewsdetector.ui.utils.eventAggregator.events.NavigateToEvent
import com.yunhao.fakenewsdetector.ui.utils.eventAggregator.subscribe
import com.yunhao.fakenewsdetector.ui.view.common.FragmentBase
import com.yunhao.fakenewsdetector.ui.viewmodel.LoginViewModel
import com.yunhao.fakenewsdetector.ui.viewmodel.UserViewModel
import com.yunhao.fakenewsdetector.ui.viewmodel.common.ViewModelBase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment: FragmentBase<FragmentLoginBinding, ViewModelBase>() {

    // Dependency Injection
    @Inject
    lateinit var dialogsManager: DialogsManager

    @Inject
    lateinit var eventAggregator: EventAggregator
    // ---

    override val viewModel: LoginViewModel by viewModels()
    private val userViewModel: UserViewModel by activityViewModels()

    override fun getLayoutId(): Int = R.layout.fragment_login

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun setUpListeners() {
        binding?.signUpButton?.setOnClickListener {
            findNavController().navigate(R.id.action_LoginFragment_to_signUpFragment)
        }

        dialogsManager.subscribeToEvents(requireActivity(), viewLifecycleOwner)
    }

    override fun setUpSubscribers() {
        eventAggregator.subscribe<NavigateToEvent>(viewLifecycleOwner) {
            findNavController().navigate(it.resId)
        }
    }
}
