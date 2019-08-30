package me.alfredobejarano.golfassistant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import me.alfredobejarano.golfassistant.databinding.FragmentScorecardListBinding
import me.alfredobejarano.golfassistant.viewmodels.ScorecardListViewModel

class ScorecardListFragment : Fragment() {
    private lateinit var viewModel: ScorecardListViewModel
    private lateinit var binding: FragmentScorecardListBinding

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, state: Bundle?): View =
        FragmentScorecardListBinding.inflate(inflater, parent, false).apply {
            binding = this
            binding.matchRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            viewModel =
                ViewModelProviders.of(this@ScorecardListFragment)[ScorecardListViewModel::class.java]
        }.root

    private fun fetchScoreCardList() = viewModel.getScorecardList().observe(this, Observer { list ->
        binding.emptyListGroup.visibility = if (list.isNullOrEmpty()) {
            View.VISIBLE
        } else {
            View.GONE
        }
    })
}