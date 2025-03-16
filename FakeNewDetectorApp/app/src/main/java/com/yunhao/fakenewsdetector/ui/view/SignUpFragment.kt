package com.yunhao.fakenewsdetector.ui.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.yunhao.fakenewsdetector.R
import com.yunhao.fakenewsdetector.databinding.FragmentSignUpBinding
import com.yunhao.fakenewsdetector.ui.view.common.FragmentBase
import com.yunhao.fakenewsdetector.ui.viewmodel.SignUpViewModel
import com.yunhao.fakenewsdetector.ui.viewmodel.common.ViewModelBase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import androidx.core.widget.addTextChangedListener

@AndroidEntryPoint
class SignUpFragment : FragmentBase<FragmentSignUpBinding, ViewModelBase>() {

    override val viewModel: SignUpViewModel by viewModels()

    override fun getLayoutId(): Int = R.layout.fragment_sign_up

    private var datePicker: MaterialDatePicker<Long>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpListeners()
        setUpObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun setUpListeners() {
        binding?.birthdateText?.setOnClickListener {
            showDatePickerDialog()
        }

        binding?.signUpButton?.setOnClickListener {
            lifecycleScope.launch {
                viewModel.signUp{
                    if (it){
                        findNavController().navigate(R.id.action_signUpFragment_to_LoginFragment)
                    }
                    else{
                        // TODO: show error popup
                    }
                }
            }
        }

        binding?.logInButton?.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_LoginFragment)
        }

        binding?.nameInputText?.addTextChangedListener {
            viewModel.name.value = it.toString()
        }

        binding?.lastNameInputText?.addTextChangedListener {
            viewModel.lastname.value = it.toString()
        }

        binding?.birthdateText?.addTextChangedListener {
            viewModel.birthdate.value = it.toString()
        }

        binding?.emailInputText?.addTextChangedListener {
            viewModel.email.value = it.toString()
        }

        binding?.passwordInputText?.addTextChangedListener {
            viewModel.password.value = it.toString()
        }

        binding?.passwordConfirmInputText?.addTextChangedListener {
            viewModel.confirmPassword.value = it.toString()
        }
    }

    override fun setUpObservers() {
        viewModel.nameError.observe(viewLifecycleOwner) {
            binding?.nameInputText?.error = it
        }

        viewModel.lastnameError.observe(viewLifecycleOwner) {
            binding?.lastNameInputText?.error = it
        }

        viewModel.birthdateError.observe(viewLifecycleOwner) {
            binding?.birthdateText?.error = it
        }

        viewModel.emailError.observe(viewLifecycleOwner) {
            binding?.emailInputText?.error = it
        }

        viewModel.passwordError.observe(viewLifecycleOwner) {
            binding?.passwordInputText?.error = it
        }

        viewModel.confirmPasswordError.observe(viewLifecycleOwner) {
            binding?.passwordConfirmInputText?.error = it
        }

        viewModel.isSignUpEnabled.observe(viewLifecycleOwner) {
            binding?.signUpButton?.isEnabled = it
        }
    }

    private fun showDatePickerDialog() {
        if (datePicker == null) {
            datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(context?.getString(R.string.select_birthdate))
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

            datePicker!!.addOnPositiveButtonClickListener { selection ->
                val date = localDateFromTimestamp(selection).toString()
                binding?.birthdateText?.setText(date)
                viewModel.birthdate.value = date
            }
        }

        if (!datePicker!!.isAdded) {
            datePicker!!.show(requireActivity().supportFragmentManager, null)
        }
    }

    private fun localDateFromTimestamp(timestamp: Long): LocalDate {
        return Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDate()
    }
}
