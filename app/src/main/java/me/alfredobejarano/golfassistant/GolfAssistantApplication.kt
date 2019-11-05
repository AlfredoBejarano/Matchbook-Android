package me.alfredobejarano.golfassistant

import android.app.Application
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import me.alfredobejarano.golfassistant.injection.Injector
import javax.inject.Inject

class GolfAssistantApplication : Application(), HasAndroidInjector {
    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector() = fragmentInjector

    override fun onCreate() {
        Injector.init(this)
        Injector.component.inject(this)
        super.onCreate()
    }
}