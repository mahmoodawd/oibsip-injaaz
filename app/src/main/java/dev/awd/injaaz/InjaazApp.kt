package dev.awd.injaaz

import android.app.Application
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class InjaazApp() : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}