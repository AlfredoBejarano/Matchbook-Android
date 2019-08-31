package me.alfredobejarano.golfassistant.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import me.alfredobejarano.golfassistant.data.Scorecard
import me.alfredobejarano.golfassistant.databinding.ItemMatchBinding

class ScorecardAdapter(
    private var scorecardList: List<Scorecard>,
    private val onItemClick: (itemId: Long) -> Unit
) :
    Adapter<ScorecardAdapter.ScorecardViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ScorecardViewHolder(
            ItemMatchBinding
                .inflate(LayoutInflater.from(parent.context), parent, false),
            onItemClick
        )

    override fun getItemCount() = scorecardList.size

    fun getItemAtPosition(position: Int) = scorecardList[position]

    fun updateList(newList: List<Scorecard>) {
        val diffUtilCallback = ScoreCardDiffUtilCalback(scorecardList, newList)
        DiffUtil.calculateDiff(diffUtilCallback).dispatchUpdatesTo(this)
        scorecardList = newList
    }

    override fun onBindViewHolder(holder: ScorecardViewHolder, position: Int) =
        holder.bind(scorecardList[position])


    class ScoreCardDiffUtilCalback(
        private val old: List<Scorecard>,
        private val nw: List<Scorecard>
    ) :
        DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            old[oldItemPosition].compareTo(nw[newItemPosition]) == 0

        override fun getOldListSize() = old.size

        override fun getNewListSize() = nw.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            areItemsTheSame(oldItemPosition, newItemPosition) &&
                    old[oldItemPosition].rows.size == nw[newItemPosition].rows.size
    }

    class ScorecardViewHolder(
        private val binding: ItemMatchBinding,
        private val onItemClick: (itemId: Long) -> Unit
    ) :
        ViewHolder(binding.root) {
        fun bind(scorecard: Scorecard) {
            binding.scorecard = scorecard
            binding.root.setOnClickListener {
                onItemClick(scorecard.id)
            }
        }


    }
}