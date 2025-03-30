package com.yunhao.fakenewsdetector.ui.view

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.yunhao.fakenewsdetector.R
import com.yunhao.fakenewsdetector.databinding.FragmentSignUpBinding
import com.yunhao.fakenewsdetector.ui.utils.DialogsManager
import com.yunhao.fakenewsdetector.ui.utils.eventAggregator.EventAggregator
import com.yunhao.fakenewsdetector.ui.utils.eventAggregator.events.NavigateToEvent
import com.yunhao.fakenewsdetector.ui.utils.eventAggregator.subscribe
import com.yunhao.fakenewsdetector.ui.view.common.FragmentBase
import com.yunhao.fakenewsdetector.ui.viewmodel.SignUpViewModel
import com.yunhao.fakenewsdetector.ui.viewmodel.common.ViewModelBase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

@AndroidEntryPoint
class SignUpFragment: FragmentBase<FragmentSignUpBinding, ViewModelBase>() {

    // Dependency Injection
    @Inject
    lateinit var dialogsManager: DialogsManager

    @Inject
    lateinit var eventAggregator: EventAggregator
    // ---

    override val viewModel: SignUpViewModel by viewModels()

    override fun getLayoutId(): Int = R.layout.fragment_sign_up

    private var datePicker: MaterialDatePicker<Long>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpListeners()
        setUpObservers()
        setUpSubscribers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun setUpListeners() {
        binding?.birthdateText?.setOnClickListener {
            showDatePickerDialog()
        }

        binding?.logInButton?.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_LoginFragment)
        }

        binding?.nameInputText?.addTextChangedListener {
            if (false == viewModel.nameTouched.value) {
                viewModel.nameTouched.value = true
            }

            viewModel.name.value = it.toString()
        }

        binding?.lastNameInputText?.addTextChangedListener {
            if (false == viewModel.lastNameTouched.value) {
                viewModel.lastNameTouched.value = true
            }

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

    override fun setUpSubscribers() {
        super.setUpSubscribers()

        eventAggregator.subscribe<NavigateToEvent>(viewLifecycleOwner, true) {
            findNavController().navigate(it.resId)
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
