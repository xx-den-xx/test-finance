package com.bda.finance_test.app

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.bda.finance_test.app.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            androidLogger(Level.ERROR)
            modules (listOf(
                viewModelModule,
                databaseModule,
                pairRepositoryModule,
                webSocketModule
            ))
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }


}