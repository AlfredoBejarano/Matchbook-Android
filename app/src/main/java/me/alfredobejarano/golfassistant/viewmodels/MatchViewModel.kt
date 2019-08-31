package me.alfredobejarano.golfassistant.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ioViewModelScope
import kotlinx.coroutines.launch
import me.alfredobejarano.golfassistant.data.Scorecard
import me.alfredobejarano.golfassistant.data.ScorecardRepository
import me.alfredobejarano.golfassistant.data.ScorecardRow
import javax.inject.Inject

class MatchViewModel @Inject constructor(private val repository: ScorecardRepository) :
    ViewModel() {

    private suspend fun getScoreCardById(scoreCardId: Long): Scorecard? = try {
        repository.getScoreCards().first { it.id == scoreCardId }
    } catch (e: Exception) {
        null
    }

    fun retrieveScorecardName(scoreCardId: Long): LiveData<String> {
        val mediator = MediatorLiveData<String>()
        ioViewModelScope.launch {
            val scoreCard = getScoreCardById(scoreCardId) ?: return@launch
            mediator.postValue(scoreCard.playerName)
        }
        return mediator
    }

    fun retrieveScorecardRows(scoreCardId: Long): LiveData<List<ScorecardRow>> {
        val mediator = MediatorLiveData<List<ScorecardRow>>()
        ioViewModelScope.launch {
            val scoreCard = getScoreCardById(scoreCardId) ?: return@launch
            val rows = scoreCard.rows
            mediator.postValue(rows)
        }
        return mediator
    }

    fun createScorecardRow(
        scoreCardId: Long,
        handicap: Int,
        match: Int,
        won: Float,
        loss: Float
    ): LiveData<List<ScorecardRow>> {
        val mediator = MediatorLiveData<List<ScorecardRow>>()
        ioViewModelScope.launch {
            repository.addNewRowToScorecard(scoreCardId, handicap, match, won, loss)
            val scorecard = getScoreCardById(scoreCardId)
            mediator.postValue(scorecard?.rows)
        }
        return mediator
    }
}