package com.example.custommaplocationserviceapplication

import android.app.Application
import android.arch.lifecycle.ProcessLifecycleOwner

class MyApplication : Application() {
    private lateinit var activityLifecycleTracker: ActivityLifecycleTracker

    override fun onCreate() {
        super.onCreate()
        activityLifecycleTracker = ActivityLifecycleTracker(this)

        registerActivityLifecycleCallbacks(activityLifecycleTracker)
        ProcessLifecycleOwner.get().lifecycle.addObserver(activityLifecycleTracker)
    }
}