package me.alfredobejarano.golfassistant.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import me.alfredobejarano.golfassistant.data.ScorecardRow
import me.alfredobejarano.golfassistant.databinding.ItemScorecardRowBinding

class ScorecardRowAdapter(private var rows: List<ScorecardRow>) :
    RecyclerView.Adapter<ScorecardRowAdapter.ScorecardRowViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ScorecardRowViewHolder(
            ItemScorecardRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun getItemCount() = rows.size

    override fun onBindViewHolder(holder: ScorecardRowViewHolder, position: Int) =
        holder.bind(rows[position])

    fun updateList(newList: List<ScorecardRow>) {
        val diffUtilCallback = ScorecardRowDiffUtilCallback(rows, newList)
        DiffUtil.calculateDiff(diffUtilCallback).dispatchUpdatesTo(this)
        rows = newList
    }

    fun getItemAtPosition(position: Int) = rows[position]

    class ScorecardRowDiffUtilCallback(
        private val old: List<ScorecardRow>,
        private val nw: List<ScorecardRow>
    ) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            old[oldItemPosition].compareTo(nw[newItemPosition]) == 0

        override fun getOldListSize() = old.size

        override fun getNewListSize() = nw.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            areItemsTheSame(oldItemPosition, newItemPosition)
    }

    class ScorecardRowViewHolder(private val binding: ItemScorecardRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(row: ScorecardRow) {
            binding.row = row
        }
    }
}