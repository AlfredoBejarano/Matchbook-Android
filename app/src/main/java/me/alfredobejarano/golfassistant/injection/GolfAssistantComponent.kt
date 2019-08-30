package me.alfredobejarano.golfassistant.injection

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ViewModelModule::class,
        UIControllerModule::class,
        LocalDataSourceModule::class,
        AndroidInjectionModule::class,
        AndroidSupportInjectionModule::class
    ]
)
interface GolfAssistantComponent {
    @Component.Builder
    interface Builder {
        fun build(): GolfAssistantComponent
        @BindsInstance
        fun setApplication(application: Application): Builder

        fun setLocalDataSourceModule(localDataSourceModule: LocalDataSourceModule): Builder
    }
}