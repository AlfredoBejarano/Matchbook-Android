package me.alfredobejarano.golfassistant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
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
    @Inject
    lateinit var factory: ViewModelFactory
    private lateinit var viewModel: MatchViewModel
    private lateinit var binding: FragmentMatchBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentMatchBinding.inflate(inflater, container, false).apply {
        AndroidSupportInjection.inject(this@MatchFragment)
        binding = this
        viewModel = ViewModelProviders.of(this@MatchFragment, factory)[MatchViewModel::class.java]

        scorecardRowList.layoutManager = LinearLayoutManager(context)
        getScorecardPlayerName()
        getScorecardRows()
        setupAddButton()
    }.root

    private fun getScorecardId() = MatchFragmentArgs.fromBundle(arguments ?: Bundle()).scorecardId

    private fun getScorecardPlayerName() =
        viewModel.retrieveScorecardName(getScorecardId()).observe(this, Observer<String> { name ->
            requireActivity().title =
                String.format(Locale.getDefault(), getString(R.string.match_with_title), name)
        })

    private fun getScorecardRows() = viewModel.retrieveScorecardRows(getScorecardId())
        .observe(this, Observer<List<ScorecardRow>> { rows -> drawRows(rows) })

    private fun drawRows(rows: List<ScorecardRow>) = binding.scorecardRowList.apply {
        (adapter as? ScorecardRowAdapter)?.updateList(rows) ?: run {
            adapter = ScorecardRowAdapter(rows)
        }
    }

    private fun setupAddButton() = binding.createRowButton.setOnClickListener {
        launchAddRowFragment()
    }

    private fun launchAddRowFragment() {
        val fragment = CreateRowFragment()
        fragment.addButtonListener { handicap, match, won, loss ->
            addScoreCardRow(handicap, match, won, loss)
        }
        fragment.show(requireFragmentManager(), CreateRowFragment.SHOW_TAG)
    }

    private fun addScoreCardRow(handicap: Int, match: Int, won: Float, loss: Float) =
        viewModel.createScorecardRow(getScorecardId(), handicap, match, won, loss)
            .observe(this, Observer { drawRows(it) })
}