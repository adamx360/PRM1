package com.example.prm1

import android.app.Application
import com.example.prm1.data.RepositoryLocator

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        RepositoryLocator.init(applicationContext)
    }
}
