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
            fetchScoreCardList()
        }.root

    private fun injectViewModelFactory() {
        AndroidSupportInjection.inject(this)
        viewModel = ViewModelProviders.of(this, factory)[ScorecardListViewModel::class.java]
    }

    private fun fetchScoreCardList() = viewModel.getScorecardList().observe(this, Observer { list ->
        binding.emptyListGroup.visibility = if (list.isNullOrEmpty()) {
            View.VISIBLE
        } else {
            View.GONE
        }
    })
}