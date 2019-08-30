package me.alfredobejarano.golfassistant

import android.app.Application
import me.alfredobejarano.golfassistant.injection.Injector

class GolfAssistantApplication : Application() {
    override fun onCreate() {
        Injector.init(this)
        super.onCreate()
    }
}