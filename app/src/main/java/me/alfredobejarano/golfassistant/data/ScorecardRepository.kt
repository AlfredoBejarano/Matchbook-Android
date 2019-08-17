package me.alfredobejarano.golfassistant.data

import java.text.SimpleDateFormat
import java.util.*

class ScorecardRepository(private val scorecardDAO: ScorecardDAO) {
    /**
     * Retrieves all the games that the user has been keeping track of.
     */
    suspend fun getScoreCards() = scorecardDAO.readAll()

    /**
     * Creates a [Scorecard] using the given player name as the player for said [Scorecard].
     */
    suspend fun createScorecardForPlayer(playerName: String) {
        val scoreCard = Scorecard(playerName = playerName)
        scorecardDAO.createOrUpdate(scoreCard)
    }

    /**
     * Add a [ScorecardRow] to the rows of the currently played [Scorecard].
     * @param scorecardId Id of the currently played [Scorecard]
     * @param row The row to be added.
     */
    suspend fun updateScorecardRows(scorecardId: Long, row: ScorecardRow, deleteRow: Boolean): Scorecard? {
        val scorecard = scorecardDAO.read(scorecardId) ?: return null
        val rows = scorecard.rows as? MutableList<ScorecardRow> ?: return null

        if (deleteRow) {
            rows.remove(row)
        } else {
            rows.add(row)
        }

        val updatedScorecard = Scorecard(scorecardId, scorecard.playerName, rows)
        scorecardDAO.createOrUpdate(updatedScorecard)

        return updatedScorecard
    }

    /**
     * Retrieves the current moment in time and formats it as a String using the dd/MM/yyyy pattern.
     */
    private fun generateDate() = synchronized(this) {
        val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        dateFormatter.format(Date())
    }

    /**
     * Adds an empty row to a given [Scorecard] by passing its id.
     */
    suspend fun addEmptyRowToScorecard(scorecardId: Long): Scorecard? {
        val row = ScorecardRow(date = generateDate())
        return updateScorecardRows(scorecardId, row, false)
    }
}