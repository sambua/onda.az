package az.onda.project

import android.app.Application
import az.onda.di.initKoinModule
import org.koin.android.ext.koin.androidContext

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize Koin after super.onCreate() so Android context is ready
        initKoinModule (
            config = {
                androidContext(this@MyApplication)
            }
        )
    }
}
