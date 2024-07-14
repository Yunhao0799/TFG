package com.yunhao.fakenewsdetector.ui.view

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.yunhao.fakenewsdetector.R
import com.yunhao.fakenewsdetector.databinding.FragmentLoginBinding
import com.yunhao.fakenewsdetector.ui.view.common.FragmentBase
import com.yunhao.fakenewsdetector.ui.viewmodel.LoginViewModel
import com.yunhao.fakenewsdetector.ui.viewmodel.UserViewModel
import com.yunhao.fakenewsdetector.ui.viewmodel.common.ViewModelBase
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class LoginFragment : FragmentBase<FragmentLoginBinding, ViewModelBase>() {

    override val viewModel: LoginViewModel by viewModels()

    private val userViewModel: UserViewModel by activityViewModels()

    override fun getLayoutId(): Int {
        return R.layout.fragment_login
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.welcomeButton.setOnClickListener()
//        binding.welcomeTextView.text = viewModel.welcomeText
//        binding.buttonFirst.setOnClickListener {
//            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
//        }
//        binding.welcomeButton.setOnClickListener{
//            viewLifecycleOwner.lifecycleScope.launch { viewModel.testFun() }
//        }

        setUpListeners()

        setUpObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun setUpListeners() {
        binding?.signUpButton?.setOnClickListener{
            findNavController().navigate(R.id.action_LoginFragment_to_signUpFragment)
        }

        binding?.logInButton?.setOnClickListener{
            if (!canLogIn()){
                return@setOnClickListener
            }

            login(binding?.emailInputText?.text.toString(), binding?.passwordInputText?.text.toString())
            userViewModel.isUserLoggedIn.value = true
        }

        binding?.emailInputText?.addTextChangedListener{
            val email = binding?.emailInputText?.text.toString()
            if (email != "") {
                if (!isValidEmail(email)) {
                    binding?.emailInput?.error = getString(R.string.invalid_email)
                } else {
                    binding?.emailInput?.error = null
                }
            }
            else {
                binding?.emailInput?.error = null
            }

            canLogIn()
        }

        binding?.passwordInputText?.addTextChangedListener{
            val password = binding?.passwordInputText?.text.toString()
            if (password == "") {
                binding?.passwordInput?.error = getString(R.string.invalid_pass)
            } else {
                binding?.passwordInput?.error = null
            }

            canLogIn()
        }
    }

    override fun setUpObservers(){
        //canLogIn.observe(viewLifecycleOwner, Observer {
            //binding?.logInButton?.isEnabled = it
        //})
    }

    private fun login(username: String?, password: String?) {
        viewModel.login(username ?: "", password ?: "" ){success ->
            if (success){
                userViewModel.isUserLoggedIn.value = true
                findNavController().navigate(R.id.action_LoginFragment_to_mainFragment)
                clearAuthenticationError()
            }
            else{
                showAuthenticationError()
            }

        }
    }

    private fun isValidEmail(email: String): Boolean{
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun showAuthenticationError(){
        binding?.emailInput?.error = " "
        binding?.passwordInput?.error =  getString(R.string.authentication_error)
        canLogIn()
    }

    private fun clearAuthenticationError(){
        binding?.emailInput?.error = null
        binding?.passwordInput?.error = null
        canLogIn()
    }

    private fun canLogIn() : Boolean{
        return binding?.emailInput?.error == null &&
                         binding?.passwordInput?.error == null
    }

}