package kr.co.helicopark.movienoti

import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class Application : android.app.Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.IS_DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}