package me.alfredobejarano.golfassistant.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ioViewModelScope
import kotlinx.coroutines.launch
import me.alfredobejarano.golfassistant.data.Scorecard
import me.alfredobejarano.golfassistant.data.ScorecardRepository

class ScorecardListViewModel(private val repository: ScorecardRepository) : ViewModel() {
    /**
     * Retrieves a list of stored Scorecard matches.
     */
    fun getScorecardList(): LiveData<List<Scorecard>> {
        val mediator = MediatorLiveData<List<Scorecard>>()
        ioViewModelScope.launch { mediator.postValue(repository.getScoreCards()) }
        return mediator
    }
}