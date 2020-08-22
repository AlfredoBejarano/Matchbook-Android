package me.alfredobejarano.golfassistant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import me.alfredobejarano.golfassistant.adapters.ScorecardRowAdapter
import me.alfredobejarano.golfassistant.data.MatchResult
import me.alfredobejarano.golfassistant.data.ScorecardRow
import me.alfredobejarano.golfassistant.databinding.FragmentMatchBinding
import me.alfredobejarano.golfassistant.utils.viewBinding
import me.alfredobejarano.golfassistant.viewmodels.MatchViewModel
import java.util.Locale

@AndroidEntryPoint
class MatchFragment : Fragment() {
    private var withHandicap = true

    private var predictedHandicap: Int? = null
    private val viewModel: MatchViewModel by viewModels()
    private val binding by viewBinding(FragmentMatchBinding::inflate)

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, state: Bundle?) =
        binding.root

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.run {
            scorecardRowList.layoutManager = LinearLayoutManager(context)
            getScorecardId()
            observePredictedHandicap()
            getScorecardPlayerName()
            getScorecardRows()
            setupAddButton()
        }
    }

    private fun getScorecardId() =
        viewModel.setScoreCardId(MatchFragmentArgs.fromBundle(arguments ?: Bundle()).scorecardId)

    private fun getScorecardPlayerName() =
        viewModel.retrieveScorecardName().observe(
            viewLifecycleOwner, Observer<String> { name ->
                requireActivity().title =
                    String.format(Locale.getDefault(), getString(R.string.match_with_title), name)
            })

    private fun getScorecardRows() = viewModel.retrieveScorecardRows()
        .observe(viewLifecycleOwner, Observer { rows -> rows?.run(::drawRows) })

    private fun drawRows(rows: List<ScorecardRow>) {
        withHandicap = rows.isNullOrEmpty()
        binding.scorecardRowList.apply {
            (adapter as? ScorecardRowAdapter)?.updateList(rows) ?: run {
                adapter = ScorecardRowAdapter(rows)
            }
        }
    }

    private fun setupAddButton() = binding.createRowButton.setOnClickListener {
        launchAddRowFragment()
    }

    private fun launchAddRowFragment() {
        val fragment = CreateRowFragment()
        fragment.setHandicap(withHandicap)
        fragment.addButtonListener(::addScoreCardRow)
        fragment.show(childFragmentManager, CreateRowFragment.SHOW_TAG)
    }

    private fun addScoreCardRow(bet: Double, match: String, result: MatchResult, handicap: Int?) =
        viewModel.createScorecardRow(bet, match, result, handicap).observe(
            viewLifecycleOwner, Observer { it?.run(::drawRows) })

    private fun observePredictedHandicap() = viewModel.nextHandicapLiveData.observe(
        viewLifecycleOwner,
        Observer(::setPredictedHandicapBadge)
    )

    private fun setPredictedHandicapBadge(predictedHandicap: Int?) {
        this.predictedHandicap = predictedHandicap
        requireActivity().invalidateOptionsMenu()
    }

    private fun launchMatchNoteEditor() =
        viewModel.getScoreCardMessage().observe(this, Observer { note ->
            EditMatchNoteFragment().apply {
                setNote(note)
                setMessageListener(this@MatchFragment::observeNoteEdit)
            }.show(childFragmentManager, EditMatchNoteFragment.SHOW_TAG)
        })

    private fun observeNoteEdit(dialogFragment: DialogFragment, note: String) {
        viewModel.updateScorecardNote(note).observe(viewLifecycleOwner, Observer {
            it?.run { dialogFragment.dismissAllowingStateLoss() }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_match_fragment, menu)
        menu.findItem(R.id.match_handicap_badge)?.title = predictedHandicap?.toString().orEmpty()
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.match_note_edit) {
            launchMatchNoteEditor()
        }
        return super.onOptionsItemSelected(item)
    }
}