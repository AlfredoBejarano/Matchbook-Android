package me.alfredobejarano.golfassistant.viewmodels

import androidx.lifecycle.ViewModel
import me.alfredobejarano.golfassistant.data.Scorecard
import me.alfredobejarano.golfassistant.data.ScorecardRepository
import me.alfredobejarano.golfassistant.utils.ioExecute
import javax.inject.Inject

class MatchViewModel @Inject constructor(private val repository: ScorecardRepository) :
    ViewModel() {

    /**
     * Retrieves a [Scorecard] object by its database ID.
     * @param scoreCardId Id of the scorecard object to fetch.
     */
    private suspend fun getScoreCardById(scoreCardId: Long): Scorecard? = try {
        repository.getScoreCards().first { it.id == scoreCardId }
    } catch (e: Exception) {
        null
    }

    /**
     * Retrieves the player name stored in a given [Scorecard].
     * @param scoreCardId Id of the scorecard object to fetch.
     */
    fun retrieveScorecardName(scoreCardId: Long) = ioExecute {
        val scoreCard = getScoreCardById(scoreCardId) ?: Scorecard()
        scoreCard.playerName
    }

    /**
     * Retrieves the player match rows stored in a given [Scorecard].
     * @param scoreCardId Id of the scorecard object to fetch.
     */
    fun retrieveScorecardRows(scoreCardId: Long) = ioExecute {
        val scoreCard = getScoreCardById(scoreCardId) ?: Scorecard()
        val rows = scoreCard.rows
        rows
    }

    /**
     * Creates a new Row into a given scorecard object.
     * @param scoreCardId Id of the scorecard object to fetch.
     * @param won Money value won in the match.
     * @param loss Money value lost in the match.
     */
    fun createScorecardRow(
        scoreCardId: Long,
        won: Float,
        loss: Float
    ) = ioExecute {

        repository.addNewRowToScorecard(scoreCardId, won, loss)
        val scorecard = getScoreCardById(scoreCardId)
        scorecard?.rows ?: emptyList()
    }
}