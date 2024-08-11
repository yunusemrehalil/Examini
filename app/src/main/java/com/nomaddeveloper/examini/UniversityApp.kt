package com.nomaddeveloper.examini

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle

class UniversityApp : Application(), ActivityLifecycleCallbacks {
    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(this)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        //ignored
    }

    override fun onActivityStarted(activity: Activity) {
        //ignored
    }

    override fun onActivityResumed(activity: Activity) {
        //ignored
    }

    override fun onActivityPaused(activity: Activity) {
        //ignored
    }

    override fun onActivityStopped(activity: Activity) {
        //ignored
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        //ignored
    }

    override fun onActivityDestroyed(activity: Activity) {
        //ignored
    }
}