package com.sadeghtahani.notebox

import android.app.Application
import com.sadeghtahani.notebox.di.appModule
import com.sadeghtahani.notebox.features.notes.data.local.database.appContext
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class NoteboxApp : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = this

        startKoin {
            androidLogger()
            androidContext(this@NoteboxApp)
            modules(appModule)
        }
    }
}
