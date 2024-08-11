package com.nomaddeveloper.examini.util

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
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
                biometricCallback.onAuthenticationError("Biyometrik durumu bilinmiyor.")
                false
            }

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                biometricCallback.onAuthenticationError("Biyometrik donanım tespit edilmedi.")
                false
            }

            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                biometricCallback.onAuthenticationError("Biyometrik donanım kullanılamıyor.")
                false
            }

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                biometricCallback.onAuthenticationError("Biyometrik kimlik bilgileri kaydedilmemiş.")
                false
            }

            else -> {
                biometricCallback.onAuthenticationError("Biyometrik kimlik doğrulama kullanılamıyor.")
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
            .setTitle("Oturumunuzu doğrulayın.")
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
                "Telefonunuzda güvenlik doğrulaması yok, Google ile giriş yapın."
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