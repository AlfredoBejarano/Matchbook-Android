package me.alfredobejarano.golfassistant.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import me.alfredobejarano.golfassistant.data.Scorecard
import me.alfredobejarano.golfassistant.data.ScorecardRepository
import me.alfredobejarano.golfassistant.utils.ioExecute
import javax.inject.Inject

class ScorecardListViewModel @ViewModelInject constructor(private val repository: ScorecardRepository) :
    ViewModel() {
    /**
     * Retrieves a list of stored Scorecard matches.
     * @return List of all the stored scorecards.
     */
    fun getScorecardList() = ioExecute { repository.getScoreCards() }

    /**
     * Creates a scorecard record into the local database using the given player name.
     * @param playerName Name of the player to create the scorecard.
     * @return List of all the stored scorecards with the new one included.
     */
    fun createScoreCard(playerName: String) = ioExecute {
        repository.createScorecardForPlayer(playerName)
        repository.getScoreCards()
    }

    /**
     * Creates a given scorecard from the database.
     * @param scorecard Scorecard object to be deleted.
     * @return List of all the stored scorecards without the one deleted.
     */
    fun deleteScoreCard(scorecard: Scorecard) = ioExecute {
        repository.deleteScorecard(scorecard)
        repository.getScoreCards()
    }

    /**
     * Re-adds a given Scorecard object into the database.
     * @param scorecard Scorecard object to be re-added.
     * @return List of all the stored scorecards without the one deleted.
     */
    fun restoreScorecard(scorecard: Scorecard) = ioExecute {
        repository.restoreScorecard(scorecard)
        repository.getScoreCards()
    }
}