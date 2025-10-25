package az.onda.project

import android.app.Application
import az.onda.di.initKoinModule
import org.koin.android.ext.koin.androidContext

class MyApplication: Application() {
    override fun onCreate() {
        // We can initialize things here if needed
        initKoinModule (
            config = {
                // You can declare your Koin modules here
                androidContext(this@MyApplication)
            }
        )
        super.onCreate()
    }
}
