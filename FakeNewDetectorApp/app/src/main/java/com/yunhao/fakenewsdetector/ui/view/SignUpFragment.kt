package com.yunhao.fakenewsdetector.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.viewModels
import com.google.android.material.datepicker.MaterialDatePicker
import com.yunhao.fakenewsdetector.R
import com.yunhao.fakenewsdetector.databinding.FragmentSignUpBinding
import com.yunhao.fakenewsdetector.ui.view.common.FragmentBase
import com.yunhao.fakenewsdetector.ui.viewmodel.SignUpViewModel
import com.yunhao.fakenewsdetector.ui.viewmodel.common.ViewModelBase
import dagger.hilt.android.AndroidEntryPoint
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.concurrent.TimeUnit
import kotlin.time.Duration.Companion.days
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@AndroidEntryPoint
class SignUpFragment : FragmentBase<FragmentSignUpBinding, ViewModelBase>() {

    override val viewModel: SignUpViewModel by viewModels()

    override fun getLayoutId(): Int {
        return R.layout.fragment_sign_up
    }

    private var datePicker: MaterialDatePicker<Long>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpListeners()
    }

    override fun setUpListeners() {
        binding?.birthdateText?.setOnClickListener{
            showDatePickerDialog()
        }

        binding?.signUpButton?.setOnClickListener{
            viewModel.signUp(binding?.nameInput.toString(),
                binding?.lastNameInput.toString(),
                binding?.birthdateText?.text.toString(),
                binding?.emailInput.toString(),
                binding?.passwordInput.toString())
        }
    }

    private fun showDatePickerDialog() {
        if (datePicker == null){
            datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select birthdate")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

            datePicker!!.addOnPositiveButtonClickListener{ selection ->
                var date = localDateFromTimestamp(selection).toString()
                this.binding!!.birthdateText.setText(date)
            }

        }

        if (datePicker!!.isAdded){
            return
        }

        datePicker!!.show(requireActivity().supportFragmentManager, null)
    }

    private fun localDateFromTimestamp(timestamp: Long): LocalDate = Instant.ofEpochMilli(timestamp) .atZone(
        ZoneId.systemDefault()) .toLocalDate()
}