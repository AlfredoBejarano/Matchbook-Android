package me.alfredobejarano.golfassistant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.android.support.AndroidSupportInjection
import me.alfredobejarano.golfassistant.adapters.ScorecardAdapter
import me.alfredobejarano.golfassistant.data.Scorecard
import me.alfredobejarano.golfassistant.databinding.FragmentScorecardListBinding
import me.alfredobejarano.golfassistant.injection.ViewModelFactory
import me.alfredobejarano.golfassistant.viewmodels.ScorecardListViewModel
import javax.inject.Inject

class ScorecardListFragment : Fragment() {
    @Inject
    lateinit var factory: ViewModelFactory
    private lateinit var viewModel: ScorecardListViewModel
    private lateinit var binding: FragmentScorecardListBinding

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, state: Bundle?): View =
        FragmentScorecardListBinding.inflate(inflater, parent, false).apply {
            injectViewModelFactory()
            binding = this
            binding.matchRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            setupFABButton()
            fetchScoreCardList()
        }.root

    private fun injectViewModelFactory() {
        AndroidSupportInjection.inject(this)
        viewModel = ViewModelProviders.of(this, factory)[ScorecardListViewModel::class.java]
    }

    private fun fetchScoreCardList() =
        viewModel.getScorecardList().observe(this, Observer { list -> handleEmptyListResult(list) })

    private fun displayMatches(list: List<Scorecard>) {
        binding.matchRecyclerView.adapter = ScorecardAdapter(list)
    }

    private fun handleEmptyListResult(list: List<Scorecard>?) {
        binding.emptyListGroup.visibility = if (list.isNullOrEmpty()) {
            View.VISIBLE
        } else {
            View.GONE
        }
        binding.matchRecyclerView.visibility = if (list.isNullOrEmpty()) {
            View.GONE
        } else {
            displayMatches(list)
            View.VISIBLE
        }
    }

    private fun createMatch(name: String) = viewModel.createScoreCard(name).observe(this, Observer {
        handleEmptyListResult(it)
    })


    private fun launchAddMatchFragment() =
        CreateMatchFragment().addButtonListener(this::createMatch).show(
            requireFragmentManager(),
            CreateMatchFragment.SHOW_TAG
        )

    private fun setupFABButton() =
        binding.createScorecardButton.setOnClickListener { launchAddMatchFragment() }
}