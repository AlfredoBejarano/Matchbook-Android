package me.alfredobejarano.golfassistant

import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.android.support.AndroidSupportInjection
import me.alfredobejarano.golfassistant.adapters.ScorecardRowAdapter
import me.alfredobejarano.golfassistant.data.ScorecardRow
import me.alfredobejarano.golfassistant.databinding.FragmentMatchBinding
import me.alfredobejarano.golfassistant.injection.ViewModelFactory
import me.alfredobejarano.golfassistant.viewmodels.MatchViewModel
import java.util.*
import javax.inject.Inject

class MatchFragment : Fragment() {
    private var withHandicap = true

    @Inject
    lateinit var factory: ViewModelFactory
    private var predictedHandicap: Int? = null
    private lateinit var viewModel: MatchViewModel
    private lateinit var binding: FragmentMatchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentMatchBinding.inflate(inflater, container, false).apply {
        AndroidSupportInjection.inject(this@MatchFragment)
        binding = this
        scorecardRowList.layoutManager = LinearLayoutManager(context)
        viewModel = ViewModelProviders.of(this@MatchFragment, factory)[MatchViewModel::class.java]
        getScorecardId()
        observePredictedHandicap()
        getScorecardPlayerName()
        getScorecardRows()
        setupAddButton()
    }.root

    private fun getScorecardId() =
        viewModel.setScoreCardId(MatchFragmentArgs.fromBundle(arguments ?: Bundle()).scorecardId)

    private fun getScorecardPlayerName() =
        viewModel.retrieveScorecardName().observe(
            viewLifecycleOwner, Observer<String> { name ->
                requireActivity().title =
                    String.format(Locale.getDefault(), getString(R.string.match_with_title), name)
            })

    private fun getScorecardRows() = viewModel.retrieveScorecardRows()
        .observe(viewLifecycleOwner, Observer { rows -> drawRows(rows) })

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

    private fun addScoreCardRow(handicap: Int?, match: String, money: Float, isLoss: Boolean) {
        val won = if (isLoss) 0f else money
        val loss = if (isLoss) money else 0f
        viewModel.createScorecardRow(won, loss, match, handicap).observe(
            viewLifecycleOwner, Observer { drawRows(it) })
    }

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