package me.alfredobejarano.golfassistant.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import me.alfredobejarano.golfassistant.BuildConfig

@TypeConverters(ScorecardRowListTypeConverter::class)
@Database(entities = [Scorecard::class], version = BuildConfig.VERSION_CODE)
abstract class AppDatabase : RoomDatabase() {
    abstract fun provideScorecarDAO(): ScorecardDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        private fun buildDatabase(ctx: Context) =
            Room.databaseBuilder(ctx, AppDatabase::class.java, "${BuildConfig.APPLICATION_ID}.database")
                .fallbackToDestructiveMigration()
                .build()

        fun getInstance(ctx: Context) = INSTANCE ?: synchronized(this) {
            INSTANCE ?: buildDatabase(ctx)
        }
    }
}