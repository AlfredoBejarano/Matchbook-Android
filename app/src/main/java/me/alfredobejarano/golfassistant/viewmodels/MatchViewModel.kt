package me.alfredobejarano.golfassistant.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.alfredobejarano.golfassistant.data.Scorecard
import me.alfredobejarano.golfassistant.data.ScorecardRepository
import me.alfredobejarano.golfassistant.data.ScorecardRow
import me.alfredobejarano.golfassistant.utils.ioExecute
import javax.inject.Inject

class MatchViewModel @Inject constructor(private val repository: ScorecardRepository) :
    ViewModel() {

    private val _nextHandicapLiveData = MutableLiveData<Int>()
    val nextHandicapLiveData = _nextHandicapLiveData as LiveData<Int>

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
        predictNextMatchHandicap(rows)
    }

    /**
     * Creates a new Row into a given scorecard object.
     * @param scoreCardId Id of the scorecard object to fetch.
     * @param won Money value won in the match.
     * @param loss Money value lost in the match.
     * @param handicap Optional value for the first match.
     */
    fun createScorecardRow(
        scoreCardId: Long,
        won: Float,
        loss: Float,
        match: String,
        handicap: Int? = null
    ) = ioExecute {
        repository.addNewRowToScorecard(scoreCardId, won, loss, match, handicap)
        val scorecard = getScoreCardById(scoreCardId)
        val rows = scorecard?.rows ?: emptyList()
        predictNextMatchHandicap(rows)
    }

    /**
     * Retrieves the last match handicap and predicts what the next match handicap will be.
     * @param rows The current Scorecard match rows.
     */
    private fun predictNextMatchHandicap(rows: List<ScorecardRow>): List<ScorecardRow> {
        if (rows.isNotEmpty()) {
            val lastRow = rows.last()
            var lastHandicap = lastRow.handicap

            if (lastRow.isLoss()) {
                lastHandicap--
            } else {
                lastHandicap++
            }

            _nextHandicapLiveData.postValue(lastHandicap)
        }
        return rows
    }
}