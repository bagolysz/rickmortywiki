package org.bagolysz.rickmortywiki

import android.app.Application
import org.bagolysz.rickmortywiki.util.*
import org.koin.android.ext.android.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(
            this, listOf(
                appModule,
                presenterModule,
                fragmentModule,
                repositoryModule,
                networkModule
            )
        )
    }
}