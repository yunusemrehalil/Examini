package com.nomaddeveloper.examini.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.ai.client.generativeai.GenerativeModel
import com.nomaddeveloper.examini.BuildConfig
import com.nomaddeveloper.examini.R
import com.nomaddeveloper.examini.database.RealmCRUD
import com.nomaddeveloper.examini.listener.ConnectionProvider
import com.nomaddeveloper.examini.listener.GeminiProvider
import com.nomaddeveloper.examini.listener.GoogleProfileProvider
import com.nomaddeveloper.examini.listener.RealmProvider
import com.nomaddeveloper.examini.model.profile.GoogleProfile
import com.nomaddeveloper.examini.model.realm.GeminiPoint
import com.nomaddeveloper.examini.network.Connection
import com.nomaddeveloper.examini.ui.fragment.LoadingDialogFragment
import com.nomaddeveloper.examini.util.PreferencesUtil
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

open class BaseActivity : AppCompatActivity(), RealmProvider, GeminiProvider, ConnectionProvider,
    GoogleProfileProvider {
    private lateinit var geminiGenerativeModel: GenerativeModel
    private lateinit var loadingDialog: LoadingDialogFragment
    private lateinit var connection: Connection
    private lateinit var realm: Realm
    private lateinit var realmCRUD: RealmCRUD
    protected var googleProfile: GoogleProfile? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadingDialog = LoadingDialogFragment()
        initializeGoogleProfile()
        initializeRealm()
        initializeConnection()
        initializeGemini()
    }

    private fun initializeGoogleProfile() {
        googleProfile = PreferencesUtil.getGoogleProfile(this)
    }

    private fun initializeConnection() {
        connection = Connection()
    }

    private fun initializeGemini() {
        geminiGenerativeModel = GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = BuildConfig.GEMINI_API_KEY
        )
    }

    private fun initializeRealm() {
        val configuration = RealmConfiguration.Builder(schema = setOf(GeminiPoint::class))
            .name(getString(R.string.realm_database_name))
            .deleteRealmIfMigrationNeeded()
            .schemaVersion(1)
            .build()
        realm = Realm.open(configuration)
        realmCRUD = RealmCRUD(realm)
    }

    override fun onStop() {
        if (loadingDialog.isDialogShowing()) {
            loadingDialog.dismissAllowingStateLoss()
        }
        super.onStop()
    }

    protected fun showLoading() {
        if (!loadingDialog.isDialogShowing() && !this.isFinishing) {
            loadingDialog.show(supportFragmentManager, null)
        }
    }

    protected fun hideLoading() {
        if (loadingDialog.isDialogShowing()) {
            loadingDialog.dismissAllowingStateLoss()
        }
    }

    override fun getGemini(): GenerativeModel {
        return geminiGenerativeModel
    }

    override fun getRealm(): Realm {
        return realm
    }

    override fun getRealmCRUD(): RealmCRUD {
        return realmCRUD
    }

    override fun getConnection(): Connection {
        return connection
    }

    override fun fetchGoogleProfile(): GoogleProfile? {
        return googleProfile
    }
}