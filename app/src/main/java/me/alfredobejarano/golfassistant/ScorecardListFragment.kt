package me.alfredobejarano.golfassistant

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.AndroidSupportInjection
import me.alfredobejarano.golfassistant.adapters.ScorecardAdapter
import me.alfredobejarano.golfassistant.adapters.SwipeToDeleteCallback
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
            attachSwipeToDeleteHandler()
            fetchScoreCardList()
        }.root

    private fun injectViewModelFactory() {
        AndroidSupportInjection.inject(this)
        viewModel = ViewModelProviders.of(this, factory)[ScorecardListViewModel::class.java]
    }

    private fun fetchScoreCardList() =
        viewModel.getScorecardList().observe(
            viewLifecycleOwner,
            Observer { list -> displayScorecardListResult(list) })

    private fun displayMatches(list: List<Scorecard>) = binding.matchRecyclerView.apply {
        (adapter as? ScorecardAdapter)?.updateList(list) ?: run {
            adapter = ScorecardAdapter(list) { id ->
                val action = ScorecardListFragmentDirections.viewScorecard(id)
                findNavController().navigate(action)
            }
        }
    }

    private fun displayScorecardListResult(list: List<Scorecard>?) {
        binding.emptyListGroup.visibility = if (list.isNullOrEmpty()) {
            View.VISIBLE
        } else {
            View.GONE
        }
        binding.matchRecyclerView.visibility = if (list.isNullOrEmpty()) {
            binding.matchRecyclerView.adapter = null
            View.GONE
        } else {
            displayMatches(list)
            View.VISIBLE
        }
    }

    private fun createMatch(name: String) = viewModel.createScoreCard(name).observe(this, Observer {
        displayScorecardListResult(it)
    })


    private fun launchAddMatchFragment() =
        CreateMatchFragment().addButtonListener(this::createMatch).show(
            requireFragmentManager(),
            CreateMatchFragment.SHOW_TAG
        )

    private fun setupFABButton() =
        binding.createScorecardButton.setOnClickListener { launchAddMatchFragment() }

    private fun attachSwipeToDeleteHandler() {
        val swipeHandler = object : SwipeToDeleteCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                (binding.matchRecyclerView.adapter as? ScorecardAdapter)?.run {
                    showDeleteMatchPrompt(getItemAtPosition(viewHolder.adapterPosition))
                    notifyDataSetChanged()
                }
            }
        }
        ItemTouchHelper(swipeHandler).attachToRecyclerView(binding.matchRecyclerView)
    }

    private fun deleteScorecard(scorecard: Scorecard) = viewModel
        .deleteScoreCard(scorecard).observe(this, Observer {
            displayScorecardListResult(it)
            displayUndoDeleteSnackBar(scorecard)
        })

    private fun displayUndoDeleteSnackBar(scorecard: Scorecard) =
        Snackbar.make(binding.root, R.string.match_deleted_successfully, Snackbar.LENGTH_LONG)
            .setAction(R.string.undo) {
                restoreScorecard(scorecard)
            }
            .show()

    private fun restoreScorecard(scorecard: Scorecard) =
        viewModel.restoreScorecard(scorecard).observe(
            this,
            Observer { displayScorecardListResult(it) })

    private fun showDeleteMatchPrompt(scorecard: Scorecard) = AlertDialog.Builder(requireContext())
        .setTitle(R.string.delete_dialog_title)
        .setMessage(R.string.are_you_sure_you_want_to_delete_the_match)
        .setPositiveButton(R.string.delete) { _, _ -> deleteScorecard(scorecard) }
        .setNegativeButton(R.string.cancel) { dialog, _ -> dialog?.dismiss() }
        .show()
}