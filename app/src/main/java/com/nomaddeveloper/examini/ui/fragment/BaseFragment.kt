package com.nomaddeveloper.examini.ui.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.ai.client.generativeai.GenerativeModel
import com.nomaddeveloper.examini.database.RealmCRUD
import com.nomaddeveloper.examini.model.profile.GoogleProfile
import com.nomaddeveloper.examini.network.Connection
import com.nomaddeveloper.examini.ui.activity.BaseActivity
import io.realm.kotlin.Realm

open class BaseFragment : Fragment() {
    protected lateinit var baseActivity: BaseActivity
    protected lateinit var connection: Connection
    protected lateinit var geminiGenerativeModel: GenerativeModel
    private lateinit var loadingDialog: LoadingDialogFragment
    protected lateinit var realm: Realm
    protected lateinit var realmCRUD: RealmCRUD
    protected lateinit var googleProfile: GoogleProfile

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadingDialog = LoadingDialogFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity) {
            baseActivity = context
            realm = context.getRealm()
            realmCRUD = context.getRealmCRUD()
            geminiGenerativeModel = context.getGemini()
            connection = context.getConnection()
            googleProfile = context.fetchGoogleProfile()!!
        } else {
            throw IllegalStateException("Parent activity must be a BaseActivity")
        }
    }

    protected fun showLoading() {
        if (!loadingDialog.isDialogShowing() && !baseActivity.isFinishing) {
            loadingDialog.show(baseActivity.supportFragmentManager, null)
        }
    }

    protected fun hideLoading() {
        if (loadingDialog.isDialogShowing()) {
            loadingDialog.dismissAllowingStateLoss()
        }
    }
}