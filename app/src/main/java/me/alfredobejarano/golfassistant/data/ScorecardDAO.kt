package me.alfredobejarano.golfassistant.data

import androidx.room.*

@Dao
interface ScorecardDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createOrUpdate(scorecard: Scorecard)

    @Query("SELECT * FROM scorecards")
    suspend fun readAll(): List<Scorecard>

    @Query("SELECT * FROM scorecards WHERE pk = :id LIMIT 1")
    suspend fun read(id: Long): Scorecard?

    @Delete
    suspend fun delete(scorecard: Scorecard)
}