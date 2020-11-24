package com.roy.services.services

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import java.util.*

private const val TAG = "MyService"

class MyService : Service() {


    companion object {

        private var mRandomNumber: Int = 0
        private var mIsRandomGeneratotOn: Boolean = false


        private val MIN = 0;
        private val MAX = 100;

    }


    var mBinder: IBinder = MyServiceBinder()

    class MyServiceBinder : Binder() {

        fun getService(): MyService = MyService()

    }


    override fun onDestroy() {
        super.onDestroy()
        stopRandomNumberGenerator()
        Log.e(TAG, "Service Thread : Destroyed")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Log.e(TAG, "onStartCommand -> Service Thread id : ${Thread.currentThread().id}")
        //stopSelf()
        mIsRandomGeneratotOn = true

        Thread(
            {
                startRandomNumberGenerator()

            }).start()

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {

        Log.e(TAG, "onBind Called")
        return mBinder

    }

    override fun onUnbind(intent: Intent?): Boolean {


        Log.e(TAG, "onUnbind Called")
        return super.onUnbind(intent)


    }

    fun startRandomNumberGenerator() {

        while (mIsRandomGeneratotOn) {
            try {

                Thread.sleep(1000)
                if (mIsRandomGeneratotOn) {
                    mRandomNumber = Random().nextInt(MAX) + MIN

                    Log.e(
                        TAG,
                        "Service Thread id : ${Thread.currentThread().id} , Random Number : ${mRandomNumber}"
                    )

                }


            } catch (e: InterruptedException) {

                Log.e(
                    TAG,
                    "Thread is Interrupted"
                )
            }

        }

    }


    fun stopRandomNumberGenerator() {
        mIsRandomGeneratotOn = false
    }

    fun getRandomNumber() = mRandomNumber
}