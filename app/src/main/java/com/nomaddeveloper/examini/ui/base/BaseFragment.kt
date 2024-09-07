package com.nomaddeveloper.examini.ui.base

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.ai.client.generativeai.GenerativeModel
import com.nomaddeveloper.examini.R
import com.nomaddeveloper.examini.data.model.profile.GoogleProfile
import com.nomaddeveloper.examini.data.source.local.RealmCRUD
import com.nomaddeveloper.examini.ui.loading.LoadingDialogFragment
import com.nomaddeveloper.examini.util.PreferencesUtil
import com.nomaddeveloper.examini.util.StringUtil
import com.nomaddeveloper.examini.util.ToastUtil
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
open class BaseFragment : Fragment() {
    private lateinit var baseActivity: BaseActivity

    @Inject
    lateinit var geminiGenerativeModel: GenerativeModel

    @Inject
    lateinit var loadingDialog: LoadingDialogFragment

    @Inject
    lateinit var realmCRUD: RealmCRUD

    @Inject
    lateinit var toastUtil: ToastUtil

    @Inject
    lateinit var preferencesUtil: PreferencesUtil

    @Inject
    lateinit var stringUtil: StringUtil

    protected lateinit var googleProfile: GoogleProfile
    protected val baseViewModel: BaseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupObserver()
        googleProfile = preferencesUtil.getGoogleProfile()!!
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity) {
            baseActivity = context
        } else {
            throw IllegalStateException(getString(R.string.error_parent_activity))
        }
    }

    private fun showLoading() {
        if (::loadingDialog.isInitialized && !loadingDialog.isDialogShowing() && !baseActivity.isFinishing) {
            loadingDialog.show(baseActivity.supportFragmentManager, null)
        }
    }

    private fun hideLoading() {
        if (::loadingDialog.isInitialized && loadingDialog.isDialogShowing()) {
            loadingDialog.dismissAllowingStateLoss()
        }
    }
}