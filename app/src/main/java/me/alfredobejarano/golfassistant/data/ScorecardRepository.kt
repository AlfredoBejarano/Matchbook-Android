package me.alfredobejarano.golfassistant.data

import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class ScorecardRepository @Inject constructor(private val scorecardDAO: ScorecardDAO) {
    /**
     * Retrieves all the games that the user has been keeping track of.
     */
    suspend fun getScoreCards() = scorecardDAO.readAll()

    /**
     * Creates a [Scorecard] using the given player name as the player for said [Scorecard].
     */
    suspend fun createScorecardForPlayer(playerName: String) {
        val scoreCard = Scorecard(playerName = playerName, date = generateDate())
        scorecardDAO.createOrUpdate(scoreCard)
    }

    suspend fun restoreScorecard(scorecard: Scorecard) = scorecardDAO.createOrUpdate(scorecard)

    /**
     * Add a [ScorecardRow] to the rows of the currently played [Scorecard].
     * @param scorecardId Id of the currently played [Scorecard]
     * @param row The row to be added.
     */
    private suspend fun updateScorecardRows(
        scorecardId: Long,
        row: ScorecardRow,
        deleteRow: Boolean
    ): Scorecard? {
        val scorecard = scorecardDAO.read(scorecardId) ?: return null
        val rows = scorecard.rows as? MutableList<ScorecardRow> ?: return null
        if (deleteRow) {
            rows.remove(row)
        } else {
            setRowOrder(rows, row)
        }
        val updatedScorecard = Scorecard(scorecardId, scorecard.playerName, scorecard.date, rows)
        scorecardDAO.createOrUpdate(updatedScorecard)

        return updatedScorecard
    }

    /**
     * Configures the new row order related to the other rows or deletes the row from a row list
     * if it is set to be deleted.
     * @param rows The list of rows to be edited
     * @param row THe row that is being added.
     */
    private fun setRowOrder(rows: MutableList<ScorecardRow>, row: ScorecardRow) {
        row.order = try {
            rows.last().order + 1
        } catch (e: Exception) {
            0
        }
        rows.add(row)
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

    private fun getHandicapFrom(bet: Bet) = bet.run {
        when {
            earned > lost -> -1
            lost > earned -> +1
            else -> 0
        }
    }

    private suspend fun getLastRowFrom(scorecardId: Long) =
        scorecardDAO.read(scorecardId)?.rows?.maxBy { it.order } ?: ScorecardRow()

    suspend fun addNewRowToScorecard(scorecardId: Long, won: Float, loss: Float): Scorecard? {
        val lastRow = getLastRowFrom(scorecardId)
        val match = lastRow.match.plus(1)
        val handicap = lastRow.handicap + getHandicapFrom(lastRow.bet)

        val row = ScorecardRow(
            date = generateDate(),
            handicap = handicap,
            match = match,
            bet = Bet(won, loss)
        )
        return updateScorecardRows(scorecardId, row, false)
    }

    suspend fun deleteScorecard(scorecard: Scorecard) = scorecardDAO.delete(scorecard)
}