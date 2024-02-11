package com.yunhao.fakenewsdetector.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import com.yunhao.fakenewsdetector.R
import com.yunhao.fakenewsdetector.databinding.FragmentLoginBinding
import com.yunhao.fakenewsdetector.ui.view.common.FragmentBase
import com.yunhao.fakenewsdetector.ui.viewmodel.LoginViewModel
import com.yunhao.fakenewsdetector.ui.viewmodel.common.ViewModelBase
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class LoginFragment : FragmentBase<FragmentLoginBinding, ViewModelBase>() {

    override val viewModel: LoginViewModel by viewModels()

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}