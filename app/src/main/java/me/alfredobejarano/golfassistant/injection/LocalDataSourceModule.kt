package me.alfredobejarano.golfassistant.injection

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import me.alfredobejarano.golfassistant.data.AppDatabase

@Module
@InstallIn(ActivityRetainedComponent::class)
class LocalDataSourceModule {
    @Provides
    fun provideScorecardDAO(application: Application) =
        AppDatabase.getInstance(application).provideScorecardDAO()
}