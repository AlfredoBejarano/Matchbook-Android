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
    private var scoreCardId: Long = 0L

    private val _nextHandicapLiveData = MutableLiveData<Int>()
    val nextHandicapLiveData = _nextHandicapLiveData as LiveData<Int>

    fun setScoreCardId(id: Long?) {
        scoreCardId = id ?: 0L
    }

    /**
     * Retrieves a [Scorecard] object by its database ID.
     */
    private suspend fun getScoreCard(): Scorecard? = try {
        repository.getScoreCards().first { it.id == scoreCardId }
    } catch (e: Exception) {
        null
    }

    /**
     * Retrieves the player name stored in a given [Scorecard].
     */
    fun retrieveScorecardName() = ioExecute {
        val scoreCard = getScoreCard() ?: Scorecard()
        scoreCard.playerName
    }

    /**
     * Retrieves the player match rows stored in a given [Scorecard].
     * @param scoreCardId Id of the scorecard object to fetch.
     */
    fun retrieveScorecardRows() = ioExecute {
        val scoreCard = getScoreCard() ?: Scorecard()
        val rows = scoreCard.rows
        predictNextMatchHandicap(rows)
    }

    /**
     * Creates a new Row into a given scorecard object.
     * @param won Money value won in the match.
     * @param loss Money value lost in the match.
     * @param handicap Optional value for the first match.
     */
    fun createScorecardRow(won: Float, loss: Float, match: String, handicap: Int? = null) =
        ioExecute {
            repository.addNewRowToScorecard(scoreCardId, won, loss, match, handicap)
            val scorecard = getScoreCard()
            val rows = scorecard?.rows ?: emptyList()
            predictNextMatchHandicap(rows)
        }

    /**
     * Updates the note of the curent [Scorecard].
     */
    fun updateScorecardNote(note: String) = ioExecute {
        getScoreCard()?.run {
            this.note = note
            repository.restoreScorecard(this)
        }
    }

    /**
     * Retrieves the [Scorecard] message.
     */
    fun getScoreCardMessage() = ioExecute {
        getScoreCard()?.note
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