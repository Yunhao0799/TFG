package com.yunhao.fakenewsdetector.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.yunhao.fakenewsdetector.R
import com.yunhao.fakenewsdetector.data.model.User
import com.yunhao.fakenewsdetector.data.network.ApiClient
import com.yunhao.fakenewsdetector.databinding.FragmentWelcomeBinding
import com.yunhao.fakenewsdetector.ui.viewmodel.UserViewModel
import com.yunhao.fakenewsdetector.utils.PreferencesManager
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
@AndroidEntryPoint
class WelcomeFragment : Fragment() {

    private val userViewModel: UserViewModel by activityViewModels()

    private var _binding: FragmentWelcomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        PreferencesManager.initializeFrom(requireContext())

        userViewModel.isUserLoggedIn.observe(viewLifecycleOwner, Observer { isLoggedIn ->
            if (isLoggedIn){
                findNavController().navigate(R.id.action_WelcomeFragment_to_mainFragment)
            }
            else{

            }
        })

        setUpListeners()

        ApiClient.instance
            .getCsrf()
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        // Handle successful response
                    } else {
                        // Handle failure
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    // Handle error
                }
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpListeners(){
        binding.welcomeButton.setOnClickListener{
            findNavController().navigate(R.id.action_WelcomeFragment_to_LoginFragment)
        }

    }
}