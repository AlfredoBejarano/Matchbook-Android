package me.alfredobejarano.golfassistant.injection

import android.app.Application
import dagger.Module
import dagger.Provides
import me.alfredobejarano.golfassistant.data.AppDatabase

@Module
class LocalDataSourceModule(private val application: Application) {
    @Provides
    fun provideScorecardDAO() = AppDatabase.getInstance(application).provideScorecarDAO()
}