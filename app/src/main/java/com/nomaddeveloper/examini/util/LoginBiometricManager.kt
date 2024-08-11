package com.nomaddeveloper.examini.util

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.nomaddeveloper.examini.R
import java.util.concurrent.Executor

class LoginBiometricManager(val context: Context, val biometricCallback: BiometricCallback) {
    private val executor: Executor = ContextCompat.getMainExecutor(context)
    private val biometricManager = BiometricManager.from(context)
    private val allowedAuthenticators: Int =
        BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.BIOMETRIC_WEAK or BiometricManager.Authenticators.DEVICE_CREDENTIAL

    private fun checkIfBiometricAvailable(): Boolean {
        return when (biometricManager.canAuthenticate(allowedAuthenticators)) {
            BiometricManager.BIOMETRIC_SUCCESS -> true
            BiometricManager.BIOMETRIC_STATUS_UNKNOWN -> {
                biometricCallback.onAuthenticationError(context.getString(R.string.biometric_status_unknown))
                false
            }

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                biometricCallback.onAuthenticationError(context.getString(R.string.biometric_error_no_hardware))
                false
            }

            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                biometricCallback.onAuthenticationError(context.getString(R.string.biometric_error_hw_unavailable))
                false
            }

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                biometricCallback.onAuthenticationError(context.getString(R.string.biometric_error_none_enrolled))
                false
            }

            else -> {
                biometricCallback.onAuthenticationError(context.getString(R.string.biometric_error_generic))
                false
            }
        }
    }

    private fun createBiometricPrompt(): BiometricPrompt {
        return BiometricPrompt(
            context as FragmentActivity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    biometricCallback.onAuthenticationSuccess()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    biometricCallback.onAuthenticationFailure()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    biometricCallback.onAuthenticationError(errorMessage = errString.toString())
                }
            })
    }

    private fun createPromptInfo(): BiometricPrompt.PromptInfo {
        return BiometricPrompt.PromptInfo.Builder()
            .setTitle(context.getString(R.string.biometric_validate_session))
            .setAllowedAuthenticators(allowedAuthenticators)
            .build()
    }

    fun authenticate() {
        if (checkIfBiometricAvailable()) {
            val biometricPrompt = createBiometricPrompt()
            val promptInfo = createPromptInfo()
            biometricPrompt.authenticate(promptInfo)
        } else {
            ToastUtil.showToast(
                context,
                context.getString(R.string.no_security_verification)
            )
            return
        }
    }

    interface BiometricCallback {
        fun onAuthenticationSuccess()
        fun onAuthenticationFailure()
        fun onAuthenticationError(errorMessage: String)
    }
}