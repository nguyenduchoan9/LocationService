package com.example.custommaplocationserviceapplication

import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.*
import android.widget.Toast
import com.google.android.gms.location.*

class LocationService : Service() {
    companion object {
        fun createIntent(context: Context) = Intent(context, LocationService::class.java)
    }

    private lateinit var binder: LocalBinder
    private lateinit var locationCallback: LocationCallback
    private var fusedLocationClient: FusedLocationProviderClient? = null

    private var lastLocation: Location? = null
    private var mServiceHandler: Handler? = null

    override fun onCreate() {
        super.onCreate()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                onNewLocation(locationResult?.lastLocation)
            }
        }

        getLastLocation()
        val handlerThread = HandlerThread("LOOOO")
        handlerThread.start()
        mServiceHandler = Handler(handlerThread.looper)
    }

    private fun getLastLocation() {
        try {
            fusedLocationClient?.lastLocation?.addOnCompleteListener { task ->
                if (task.isSuccessful && task.result != null) {
                    lastLocation = task.result
                    onNewLocation(task.result)
                }
            }
        } catch (e: SecurityException) {
            e.log()
        } catch (e: Exception) {
            e.log()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(MyNotificationManager.NOTIFICATION_ID, MyNotificationManager.getNotification(this))
        }
        return START_NOT_STICKY
    }

    override fun onRebind(intent: Intent?) {
        super.onRebind(intent)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return true
    }

    fun requestLocationUpdates(lowMode: Boolean = false) {
        try {
            fusedLocationClient?.removeLocationUpdates(locationCallback)
            fusedLocationClient?.requestLocationUpdates(
                createLocationRequest(lowMode),
                locationCallback,
                Looper.myLooper()
            )
        } catch (e: SecurityException) {
            e.log()
        }
    }

    fun removeLocationUpdates() {
        try {
            fusedLocationClient?.removeLocationUpdates(locationCallback)
        } catch (e: SecurityException) {
        }
    }

    private fun createLocationRequest(lowMode: Boolean = false): LocationRequest? {
        return LocationRequest.create()?.apply {
            val updateInterval = if (lowMode) {
                15000L
            } else {
                6000L
            }
            interval = updateInterval
            fastestInterval = updateInterval / 2
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    private fun onNewLocation(location: Location?) {
        location?.let {
            Toast.makeText(this, "lat: ${it.latitude} long: ${it.longitude}", Toast.LENGTH_LONG).show()
            "HoangA".logDebug("onNewLocation - lat: ${it.latitude} long: ${it.longitude}")
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return getLocalBinder()
    }

    private fun getLocalBinder(): LocalBinder {
        synchronized(this) {
            if (!::binder.isInitialized) {
                binder = LocalBinder()
            }
        }
        return binder
    }

    inner class LocalBinder : Binder() {
        fun getService(): LocationService = this@LocationService
    }
}
// https://www.codota.com/code/java/methods/android.location.LocationManager/requestLocationUpdates