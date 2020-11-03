package org.vernality.profitclub.application

import android.app.Application
import com.parse.Parse
import org.vernality.profitclub.di.application
import org.vernality.profitclub.di.viewModelDependency
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class App : Application() {

    override fun onCreate() {
        super.onCreate()

        initParse()

        startKoin {
            androidContext(this@App)
            modules(listOf(application, viewModelDependency))
        }
    }


    private fun initParse(){
        Parse.initialize(
            Parse.Configuration.Builder(this)
                .applicationId("org.vernality.alliance") // if defined
                .clientKey("hWlREY7dvWb7sLpCVfZrReWNKPHh4uJT")
                .server("https://alliance.vernality.net/parse")
                .build()
        )
    }
}