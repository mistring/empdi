package net.mistring.empdi

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Timber logging
        if (BuildConfig.DEBUG) {
            Timber.plant(MyDebugTree())
        }
    }

}

class MyDebugTree : Timber.DebugTree() {
    override fun createStackElementTag(element: StackTraceElement): String {
        return String.format(
            "[C:%s] [M:%s] [L:%s]",
            super.createStackElementTag(element),
            element.methodName,
            element.lineNumber
        )
    }
}