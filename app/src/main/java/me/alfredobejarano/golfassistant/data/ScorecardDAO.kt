package me.alfredobejarano.golfassistant.data

import androidx.room.*

@Dao
interface ScorecardDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createOrUpdate(scorecard: Scorecard)

    @Query("SELECT * FROM scorecards")
    fun readAll(): List<Scorecard>

    @Query("SELECT * FROM scorecards WHERE pk = :id")
    fun read(id: Long): List<Scorecard>

    @Delete
    fun delete(scorecard: Scorecard)
}