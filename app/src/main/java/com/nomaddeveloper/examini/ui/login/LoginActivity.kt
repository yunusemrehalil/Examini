package com.nomaddeveloper.examini.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.GetCredentialInterruptedException
import androidx.credentials.exceptions.GetCredentialUnknownException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.nomaddeveloper.examini.BuildConfig
import com.nomaddeveloper.examini.R
import com.nomaddeveloper.examini.data.model.profile.GoogleProfile
import com.nomaddeveloper.examini.databinding.ActivityLoginBinding
import com.nomaddeveloper.examini.ui.base.BaseActivity
import com.nomaddeveloper.examini.ui.homepage.HomePageActivity
import com.nomaddeveloper.examini.util.Constant.ONE_HOUR_IN_MILLIS
import com.nomaddeveloper.examini.util.Constant.SKIP_LOGIN
import com.nomaddeveloper.examini.util.LoginBiometricManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : BaseActivity(), View.OnClickListener {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginButtonLl: LinearLayout
    private lateinit var credentialManager: CredentialManager
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var biometricManager: LoginBiometricManager
    private var lastActivityTime: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        initBinding()
        setupUseful()
        setupUI()
        initListeners()
    }

    override fun onStart() {
        super.onStart()
        if (SKIP_LOGIN) {
            openHomePageActivity()
        } else {
            if (googleProfile != null) {
                if (System.currentTimeMillis() - lastActivityTime > ONE_HOUR_IN_MILLIS) {
                    baseViewModel.showLoading()
                    startBiometricAuthentication()
                } else {
                    openHomePageActivity()
                }
            }
        }
    }

    private fun initBinding() {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setupUseful() {
        lastActivityTime = preferencesUtil.getLastActivityTime()
        credentialManager = CredentialManager.create(this@LoginActivity)
        coroutineScope = CoroutineScope(Dispatchers.Main)
        biometricManager = LoginBiometricManager(
            this@LoginActivity,
            object : LoginBiometricManager.BiometricCallback {
                override fun onAuthenticationSuccess() {
                    openHomePageActivity()
                }

                override fun onAuthenticationFailure() {
                    //ignored, called when a biometric is valid but not recognized.
                }

                override fun onAuthenticationError(errorMessage: String) {
                    baseViewModel.hideLoading()
                    toastUtil.showToast(this@LoginActivity, errorMessage)
                }
            },
            toastUtil
        )
    }

    private fun setupUI() {
        binding.apply {
            //signupButton = mbSignup
            loginButtonLl = llLoginButton
        }
    }

    private fun initListeners() {
        loginButtonLl.setOnClickListener(this)
        //signupButton.setOnClickListener(this)
    }

    private fun googleSignIn() {
        baseViewModel.showLoading()
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(true)
            .setAutoSelectEnabled(false)
            .setServerClientId(BuildConfig.WEB_CLIENT_ID)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        coroutineScope.launch {
            try {
                val result = credentialManager.getCredential(
                    request = request,
                    context = this@LoginActivity
                )
                handleSignIn(result)
            } catch (e: GetCredentialException) {
                handleFailure(e)
            }
        }
    }

    private fun handleFailure(e: GetCredentialException) {
        baseViewModel.hideLoading()
        val errorMessage = when (e) {
            is GetCredentialUnknownException -> getString(R.string.unknown_error)
            is GetCredentialCancellationException -> getString(R.string.login_cancelled)
            is GetCredentialInterruptedException -> getString(R.string.login_interrupted)
            else -> e.errorMessage?.toString() ?: getString(R.string.unexpected_error)
        }
        showErrorMessage(errorMessage)
    }

    private fun handleSignIn(result: GetCredentialResponse) {
        when (val credential = result.credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential =
                            GoogleIdTokenCredential.createFrom(credential.data)
                        setGoogleProfile(googleIdTokenCredential)
                        preferencesUtil.setGoogleProfile(googleProfile!!)
                        openHomePageActivity()
                    } catch (e: GoogleIdTokenParsingException) {
                        showErrorMessage(getString(R.string.invalid_sign_in_response))
                    }
                } else {
                    showErrorMessage(getString(R.string.sign_in_error))
                }
            }

            else -> {
                showErrorMessage(getString(R.string.unexpected_error))
            }
        }
    }

    private fun showErrorMessage(message: String) {
        baseViewModel.hideLoading()
        toastUtil.showToast(this@LoginActivity, message)
    }

    private fun openHomePageActivity() {
        if (SKIP_LOGIN) {
            setMockGoogleProfile()
        }
        baseViewModel.hideLoading()
        preferencesUtil.setLastActivityTime()
        val intent = Intent(this@LoginActivity, HomePageActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val options = ActivityOptionsCompat.makeCustomAnimation(
            this,
            R.anim.slide_in_right,
            R.anim.slide_out_left
        )
        ContextCompat.startActivity(this, intent, options.toBundle())
    }

    override fun onClick(v: View?) {
        v?.id.let {
            when (it) {
                loginButtonLl.id -> {
                    //openLoginFragment()
                    googleSignIn()
                }

                /*signupButton.id -> {
                    openSignUpFragment()
                }*/
            }
        }
    }

    fun showButtons() {
        loginButtonLl.visibility = View.VISIBLE
        //signupButton.visibility = View.VISIBLE
    }

    private fun hideButtons() {
        loginButtonLl.visibility = View.GONE
        //signupButton.visibility = View.GONE
    }

    private fun startBiometricAuthentication() {
        biometricManager.authenticate()
    }

    private fun setGoogleProfile(googleIdTokenCredential: GoogleIdTokenCredential) {
        googleProfile = GoogleProfile(
            displayName = googleIdTokenCredential.displayName,
            familyName = googleIdTokenCredential.familyName,
            givenName = googleIdTokenCredential.givenName,
            id = googleIdTokenCredential.id,
            idToken = googleIdTokenCredential.idToken,
            phoneNumber = googleIdTokenCredential.phoneNumber,
            profilePictureUri = googleIdTokenCredential.profilePictureUri.toString()
        )
    }

    private fun setMockGoogleProfile() {
        googleProfile = GoogleProfile(
            displayName = "Sindirella Terligi",
            familyName = "Terligi",
            givenName = "Sindirella",
            id = "1234567890",
            idToken = "eyJhbGciOiJIzg5Md0vZlF1b3ZkQkK8t8",
            phoneNumber = "+1234567890",
            profilePictureUri = ""
        )
        preferencesUtil.setGoogleProfile(googleProfile!!)
    }
}