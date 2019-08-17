package me.alfredobejarano.golfassistant.data

import androidx.room.*

@Dao
interface ScorecardDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createOrUpdate(scorecard: Scorecard)

    @Query("SELECT * FROM scorecards")
    suspend fun readAll(): List<Scorecard>

    @Query("SELECT * FROM scorecards WHERE pk = :id")
    suspend fun read(id: Long): List<Scorecard>

    @Delete
    suspend fun delete(scorecard: Scorecard)
}