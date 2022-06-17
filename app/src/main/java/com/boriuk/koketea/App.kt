package com.boriuk.koketea

import android.app.Application
import com.google.firebase.FirebaseApp
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.DebugTree()
   }
}