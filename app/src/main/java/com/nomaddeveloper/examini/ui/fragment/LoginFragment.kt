package com.nomaddeveloper.examini.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.nomaddeveloper.examini.R
import com.nomaddeveloper.examini.databinding.FragmentLoginBinding
import com.nomaddeveloper.examini.ui.activity.HomePageActivity
import com.nomaddeveloper.examini.ui.activity.LoginActivity
import com.nomaddeveloper.examini.util.Constant
import com.nomaddeveloper.examini.util.ToastUtil

class LoginFragment : BaseFragment(), View.OnClickListener {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var loginContext: Context
    private lateinit var loginButton: MaterialButton
    private lateinit var usernameET: TextInputEditText
    private lateinit var passwordET: TextInputEditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        setupUseful()
        setupUI()
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

    private fun setupUseful() {
        loginContext = requireContext()
    }

    private fun setupUI() {
        binding.apply {
            loginButton = mbLogin
            usernameET = tietUsername
            passwordET = tietPassword
        }
        loginButton.setOnClickListener(this)
    }

    private fun openHomePageActivity() {
        val username = usernameET.text.toString()
        val intent = Intent(loginContext, HomePageActivity::class.java)
        intent.putExtra(Constant.KEY_USERNAME, username)
        startActivity(intent)
    }

    private fun checkUsernameAndPassword(): Boolean {
        return true
        // TODO()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            LoginFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onClick(v: View?) {
        val clickedItemId = v?.id
        clickedItemId.let {
            when (it) {
                loginButton.id -> if (checkUsernameAndPassword()) {
                    openHomePageActivity()
                } else {
                    ToastUtil.showToast(
                        loginContext,
                        getString(R.string.error_check_username_password)
                    )
                }
            }
        }
    }
}