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
import com.yunhao.fakenewsdetector.ui.view.common.FragmentBase
import com.yunhao.fakenewsdetector.ui.viewmodel.LoginViewModel
import com.yunhao.fakenewsdetector.ui.viewmodel.UserViewModel
import com.yunhao.fakenewsdetector.ui.viewmodel.common.ViewModelBase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@AndroidEntryPoint
class LoginFragment : FragmentBase<FragmentLoginBinding, ViewModelBase>() {

    override val viewModel: LoginViewModel by viewModels()
    private val userViewModel: UserViewModel by activityViewModels()

    override fun getLayoutId(): Int = R.layout.fragment_login

    @Singleton
    @Inject
    lateinit var dialogsManager: DialogsManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpObservers()
        setUpListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun setUpListeners() {
        binding?.signUpButton?.setOnClickListener {
            findNavController().navigate(R.id.action_LoginFragment_to_signUpFragment)
        }

        binding?.logInButton?.setOnClickListener {
            login()
        }

        binding?.emailInputText?.addTextChangedListener { text ->
            viewModel.email.value = text.toString()
        }

        binding?.passwordInputText?.addTextChangedListener { text ->
            viewModel.password.value = text.toString()
        }
    }

    override fun setUpObservers() {
        viewModel.emailError.observe(viewLifecycleOwner) { error ->
            binding?.emailInput?.error = error
        }

        viewModel.passwordError.observe(viewLifecycleOwner) { error ->
            binding?.passwordInput?.error = error
        }

        viewModel.isLoginEnabled.observe(viewLifecycleOwner) { isEnabled ->
            binding?.logInButton?.isEnabled = isEnabled
        }
    }

    private fun login() {
        lifecycleScope.launch {
            viewModel.login {
                userViewModel.isUserLoggedIn.value = it
                if (it) {
                    findNavController().navigate(R.id.action_LoginFragment_to_mainFragment)
                }
            }
        }
    }
}
