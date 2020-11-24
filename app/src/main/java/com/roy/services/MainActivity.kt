package com.roy.services

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.roy.services.services.MyService

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    lateinit var serviceIntent: Intent
    lateinit var serviceConnection: ServiceConnection
    var isServiceBound: Boolean = false
    lateinit var myService: MyService


    lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.e(TAG, "MainActivity thread id : ${Thread.currentThread().id}")

        textView = findViewById(R.id.tv_random_num)


        serviceIntent = Intent(this, MyService::class.java)

        findViewById<Button>(R.id.btn_start_service).setOnClickListener {

            startService(serviceIntent)

        }

        findViewById<Button>(R.id.btn_stop_service).setOnClickListener {

            stopService(serviceIntent)
        }

        findViewById<Button>(R.id.btn_bind_service).setOnClickListener {

            bindService()

        }

        findViewById<Button>(R.id.btn_unbind_service).setOnClickListener {

            unbindService()

        }

        findViewById<Button>(R.id.btn_get_random_num).setOnClickListener {

            getRandomNumber()

        }


    }

    fun bindService() {

        serviceConnection = object : ServiceConnection {

            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {

                Log.e(TAG, "onServiceConnected Called")

                val myServiceBinder = service as MyService.MyServiceBinder
                myService = myServiceBinder.getService()
                isServiceBound = true


            }

            override fun onServiceDisconnected(name: ComponentName?) {

                Log.e(TAG, "onServiceDisconnected Called")
                isServiceBound = false

            }
        }


        bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE)


    }

    fun unbindService() {

        if (isServiceBound) {
            unbindService(serviceConnection)
            isServiceBound = false
        }
    }

    fun getRandomNumber() {
        if (isServiceBound) {
            textView.setText("Random number : ${myService.getRandomNumber()}")
        } else {
            textView.setText("Service not bound")
        }
    }
}