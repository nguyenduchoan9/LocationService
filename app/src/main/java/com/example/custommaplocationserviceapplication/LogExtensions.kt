package com.example.custommaplocationserviceapplication

import android.util.Log

fun Exception.log() {
    Log.d("MY_TAG", message)
}

fun String.logDebug(message: String) {
    Log.d(this, message)
}

object MyLog {
    fun logDebug(TAG: String, message: String) {
        Log.d(TAG, message)
    }
}