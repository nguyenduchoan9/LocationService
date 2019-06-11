package com.example.custommaplocationserviceapplication

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class SecondScreenActivity : AppCompatActivity() {
    companion object{
        fun createIntent(context: Context) = Intent(context, SecondScreenActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second_screen)
    }
}
