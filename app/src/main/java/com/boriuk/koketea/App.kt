package com.boriuk.koketea

import android.app.Application
import timber.log.Timber.DebugTree
import timber.log.Timber.Forest.plant

class App : Application() {
    override fun onCreate() {
        super.onCreate()

//        if (BuildConfig.DEBUG) {
            plant(DebugTree())
//        } else {
//            plant(CrashReportingTree())
//        }
    }
}