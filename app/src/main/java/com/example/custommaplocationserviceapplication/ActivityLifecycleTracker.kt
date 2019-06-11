package com.example.custommaplocationserviceapplication

import android.app.Activity
import android.app.Application
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log

class ActivityLifecycleTracker(private val context: Context) : Application.ActivityLifecycleCallbacks,
    LifecycleObserver {
    private var activityStarted = 0
    private var mService: LocationService? = null
    private var mBound = false

    private val connection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            mService = null
            mBound = false
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            service?.let {
                Log.d("HoangA", "service connected")
                val binder = it as LocationService.LocalBinder
                mService = binder.getService()
                mBound = true
                mService?.requestLocationUpdates()

            }
        }
    }

    override fun onActivityStarted(activity: Activity?) {
        Log.d("HoangA", "${activity?.localClassName} started")
        if (activityStarted == 0) {
            Log.d("HoangA", "===== GO TO FOREGROUND")
            // app went to foreground
            // Bind to LocationService
            bindLocationService(activity)
        }
        activityStarted++
    }

    override fun onActivityStopped(activity: Activity?) {
        activityStarted--
        if (activityStarted == 0 && mBound) {
            // app went to background
            // Unbind to LocationService and start foreground service
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(LocationService.createIntent(context))
            } else {
                context.startService(LocationService.createIntent(context))
            }
            context.unbindService(connection)
            mBound = false
            mService = null
            Log.d("HoangA", "===== GO TO BACKGROUND")
        }
        Log.d("HoangA", "${activity?.localClassName} stopped")
    }

    private fun bindLocationService(activity: Activity?) {
        context.bindService(LocationService.createIntent(activity ?: context), connection, Context.BIND_AUTO_CREATE)
    }

    override fun onActivityPaused(activity: Activity?) {}

    override fun onActivityResumed(activity: Activity?) {}

    override fun onActivityDestroyed(activity: Activity?) {}

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {}

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {}

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onEnterForeground() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onEnterBackground() {
    }
}

// https://stackoverflow.com/questions/4414171/how-to-detect-when-an-android-app-goes-to-the-background-and-come-back-to-the-fo