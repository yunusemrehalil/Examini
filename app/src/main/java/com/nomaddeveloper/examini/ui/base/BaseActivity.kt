package com.nomaddeveloper.examini.ui.base

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.nomaddeveloper.examini.data.model.profile.GoogleProfile
import com.nomaddeveloper.examini.ui.loading.LoadingDialogFragment
import com.nomaddeveloper.examini.util.PreferencesUtil
import com.nomaddeveloper.examini.util.StringUtil
import com.nomaddeveloper.examini.util.ToastUtil
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
open class BaseActivity : AppCompatActivity() {
    @Inject
    lateinit var loadingDialog: LoadingDialogFragment

    @Inject
    lateinit var toastUtil: ToastUtil

    @Inject
    lateinit var preferencesUtil: PreferencesUtil

    @Inject
    lateinit var stringUtil: StringUtil

    protected var googleProfile: GoogleProfile? = null
    protected val baseViewModel: BaseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupObserver()
        googleProfile = preferencesUtil.getGoogleProfile()
    }

    private fun setupObserver() {
        baseViewModel.loadingState.observe(this) { isLoading ->
            if (isLoading) {
                showLoading()
            } else {
                hideLoading()
            }
        }
    }

    override fun onStop() {
        if (::loadingDialog.isInitialized && loadingDialog.isDialogShowing()) {
            loadingDialog.dismissAllowingStateLoss()
        }
        super.onStop()
    }

    private fun showLoading() {
        if (::loadingDialog.isInitialized && !loadingDialog.isDialogShowing() && !this.isFinishing) {
            loadingDialog.show(supportFragmentManager, null)
        }
    }

    private fun hideLoading() {
        if (::loadingDialog.isInitialized && loadingDialog.isDialogShowing()) {
            loadingDialog.dismissAllowingStateLoss()
        }
    }
}