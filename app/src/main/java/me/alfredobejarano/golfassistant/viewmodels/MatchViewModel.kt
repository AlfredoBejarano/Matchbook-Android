package me.alfredobejarano.golfassistant.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.alfredobejarano.golfassistant.data.MatchResult
import me.alfredobejarano.golfassistant.data.Scorecard
import me.alfredobejarano.golfassistant.data.ScorecardRepository
import me.alfredobejarano.golfassistant.utils.ioExecute

class MatchViewModel @ViewModelInject constructor(private val repository: ScorecardRepository) :
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
     */
    fun retrieveScorecardRows() = ioExecute {
        calculateNextMatchHandicap()
        getScoreCard()?.rows
    }

    /**
     * Creates a new Row into a given scorecard object.
     * @param bet Money value bet in the match.
     * @param match Played match identifier.
     * @param result Result of the played match (win, loss, tie).
     * @param handicap Handicap played in the match (null for auto-increment).
     */
    fun createScorecardRow(bet: Double, match: String, result: MatchResult, handicap: Int? = null) =
        ioExecute {
            repository.addNewRowToScorecard(scoreCardId, bet, match, result, handicap)
            val rows = getScoreCard()?.rows ?: emptyList()
            calculateNextMatchHandicap()
            rows
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
     */
    private fun calculateNextMatchHandicap() {
        viewModelScope.launch(Dispatchers.IO) {
            _nextHandicapLiveData.postValue(repository.getHandicapFrom(scoreCardId))
        }
    }
}