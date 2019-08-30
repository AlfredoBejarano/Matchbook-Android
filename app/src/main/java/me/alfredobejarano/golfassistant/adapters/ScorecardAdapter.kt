package me.alfredobejarano.golfassistant.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import me.alfredobejarano.golfassistant.data.Scorecard
import me.alfredobejarano.golfassistant.databinding.ItemMatchBinding

class ScorecardAdapter(private val scorecardList: List<Scorecard>) :
    RecyclerView.Adapter<ScorecardAdapter.ScorecardViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ScorecardViewHolder(
            ItemMatchBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun getItemCount() = scorecardList.size

    override fun onBindViewHolder(holder: ScorecardViewHolder, position: Int) =
        holder.bind(scorecardList[position])

    class ScorecardViewHolder(private val binding: ItemMatchBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(scorecard: Scorecard) {
            binding.scorecard = scorecard
        }
    }
}