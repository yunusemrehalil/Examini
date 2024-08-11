package com.nomaddeveloper.examini.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.nomaddeveloper.examini.databinding.FragmentSignupBinding
import com.nomaddeveloper.examini.ui.activity.LoginActivity

class SignupFragment : BaseFragment() {
    private lateinit var binding: FragmentSignupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignupBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    (requireActivity() as LoginActivity).showButtons()
                    requireActivity().supportFragmentManager.popBackStack()
                }
            })
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SignupFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}