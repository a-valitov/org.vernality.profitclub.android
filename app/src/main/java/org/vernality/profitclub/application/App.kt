package org.vernality.profitclub.application

import android.app.Application
import com.parse.Parse
import com.parse.ParseObject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.vernality.profitclub.di.application
import org.vernality.profitclub.di.viewModelDependency
import org.vernality.profitclub.model.data.*


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
                .server("https://alliance.vernality.net/parse")
                .build()
        )
    }
}