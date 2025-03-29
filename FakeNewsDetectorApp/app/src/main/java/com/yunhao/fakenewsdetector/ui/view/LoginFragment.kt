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

@AndroidEntryPoint
class LoginFragment: FragmentBase<FragmentLoginBinding, ViewModelBase>() {

    // Dependency Injection
    @Inject
    lateinit var dialogsManager: DialogsManager
    // ---

    override val viewModel: LoginViewModel by viewModels()
    private val userViewModel: UserViewModel by activityViewModels()

    override fun getLayoutId(): Int = R.layout.fragment_login

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

    private fun login() {
        dialogsManager.showBusyDialog(requireContext())

        lifecycleScope.launch {
            viewModel.login {
                userViewModel.isUserLoggedIn.value = it
                if (it) {
                    dialogsManager.hideCurrentDialog();
                    findNavController().navigate(R.id.action_LoginFragment_to_mainFragment)
                }
                else {
                    dialogsManager.showCustomDialog(
                        requireContext(),
                        requireContext().getString(R.string.error_title),
                        requireContext().getString(R.string.error_login)
                    )
                }
            }
        }
    }
}
