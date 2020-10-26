package org.vernality.profitclub.application

import android.app.Application
import org.vernality.profitclub.di.application
import org.vernality.profitclub.di.viewModelDependency
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(listOf(application, viewModelDependency))
        }
    }
}