package me.alfredobejarano.golfassistant.injection

import android.app.Application

object Injector {
    @Volatile
    private lateinit var app: Application

    /**
     * Initializes the application instance for the Dagger component.
     */
    fun init(application: Application) {
        app = application
    }

    /**
     * Singleton reference to the Dagger component for the app.
     */
    val component: GolfAssistantComponent by lazy {
        DaggerGolfAssistantComponent
            .builder()
            .setApplication(app)
            .setLocalDataSourceModule(LocalDataSourceModule(app))
            .build()
    }
}