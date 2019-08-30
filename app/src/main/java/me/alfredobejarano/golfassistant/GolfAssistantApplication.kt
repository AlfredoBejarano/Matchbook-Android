package me.alfredobejarano.golfassistant

import android.app.Application
import androidx.fragment.app.Fragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import me.alfredobejarano.golfassistant.injection.Injector
import javax.inject.Inject

class GolfAssistantApplication : Application(), HasSupportFragmentInjector {
    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentInjector

    override fun onCreate() {
        Injector.init(this)
        Injector.component.inject(this)
        super.onCreate()
    }
}