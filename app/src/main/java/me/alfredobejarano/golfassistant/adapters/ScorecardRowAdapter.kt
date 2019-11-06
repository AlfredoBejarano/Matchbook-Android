package me.alfredobejarano.golfassistant.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import me.alfredobejarano.golfassistant.data.ScorecardRow
import me.alfredobejarano.golfassistant.databinding.ItemScorecardRowBinding
import java.text.DecimalFormat

class ScorecardRowAdapter(private var rows: List<ScorecardRow>) :
    RecyclerView.Adapter<ScorecardRowAdapter.ScorecardRowViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ScorecardRowViewHolder(
            ItemScorecardRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun getItemCount() = rows.size

    override fun onBindViewHolder(holder: ScorecardRowViewHolder, position: Int) {
        val total = if (position == 0) {
            rows[position].total
        } else {
            var partialTotal = 0f
            for (i in 0 until position + 1) {
                partialTotal += rows[i].total
            }
            partialTotal
        }

        holder.bind(rows[position], total)
    }

    fun updateList(newList: List<ScorecardRow>) {
        val diffUtilCallback = ScorecardRowDiffUtilCallback(rows, newList)
        DiffUtil.calculateDiff(diffUtilCallback).dispatchUpdatesTo(this)
        rows = newList
    }

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

        fun bind(row: ScorecardRow, total: Float) {
            binding.total = "$${DecimalFormat("0000.##").parse(total.toString())}"
            binding.row = row
        }
    }
}