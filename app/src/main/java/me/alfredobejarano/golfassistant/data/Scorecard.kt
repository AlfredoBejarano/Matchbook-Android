package me.alfredobejarano.golfassistant.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scorecards")
data class Scorecard(
    @ColumnInfo(name = "pk")
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @ColumnInfo(name = "player_name")
    val playerName: String = "",
    val date: String = "",
    @ColumnInfo(name = "scorecard_rows")
    val rows: List<ScorecardRow> = emptyList()
) {
    fun getInitials() = try {
        val names = playerName.split(" ")
        "${names.first().first()}${names.last().first()}"
    } catch (e: Exception) {
        playerName.take(2)
    }
}