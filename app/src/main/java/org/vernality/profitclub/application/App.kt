package org.vernality.profitclub.application

import android.app.Application
import android.content.Context
import android.util.Log
import com.parse.Parse
import com.parse.ParseObject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.vernality.profitclub.BuildConfig
import org.vernality.profitclub.di.application
import org.vernality.profitclub.di.viewModelDependency
import org.vernality.profitclub.model.data.*
import timber.log.Timber
import timber.log.Timber.DebugTree


class App : Application() {



    override fun onCreate() {
        super.onCreate()

        initParse()

        startKoin {
            androidContext(this@App)
            modules(listOf(application, viewModelDependency))
        }


        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }


    }


    private fun initParse(){

        ParseObject.registerSubclass(Organization::class.java)
        ParseObject.registerSubclass(Supplier::class.java)
        ParseObject.registerSubclass(Member::class.java)
        ParseObject.registerSubclass(User::class.java)
        ParseObject.registerSubclass(Action::class.java)
        ParseObject.registerSubclass(CommercialOffer::class.java)


        Parse.initialize(
            Parse.Configuration.Builder(this)
                .applicationId("org.vernality.alliance") // if defined
                .clientKey("hWlREY7dvWb7sLpCVfZrReWNKPHh4uJT")
                .server("https://profitclub.vernality.org/parse")
                .build()
        )
    }
}
