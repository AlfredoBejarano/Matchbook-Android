package me.alfredobejarano.golfassistant.adapters

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import me.alfredobejarano.golfassistant.data.Scorecard
import me.alfredobejarano.golfassistant.databinding.ItemMatchBinding

class ScorecardAdapter(private var scorecardList: List<Scorecard>) :
    Adapter<ScorecardAdapter.ScorecardViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ScorecardViewHolder(
            ItemMatchBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun getItemCount() = scorecardList.size

    fun getItemAtPosition(position: Int) = scorecardList[position]

    fun updateList(newList: List<Scorecard>) {
        val diffUtilCalback = ScoreCardDiffUtilCalback(scorecardList, newList)
        DiffUtil.calculateDiff(diffUtilCalback).dispatchUpdatesTo(this)
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

    class ScorecardViewHolder(private val binding: ItemMatchBinding) :
        ViewHolder(binding.root) {
        fun bind(scorecard: Scorecard) {
            binding.scorecard = scorecard
        }

        abstract class SwipeToDeleteCallback(private val ctx: Context) :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            override fun onMove(rv: RecyclerView, vh: ViewHolder, t: ViewHolder) = false

            override fun onChildDraw(
                canvas: Canvas,
                recyclerView: RecyclerView,
                viewHolder: ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val root = viewHolder.itemView
                createDeleteBackground(root, dX).draw(canvas)

                super.onChildDraw(
                    canvas,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }

            private fun createDeleteBackground(root: View, dX: Float) = ColorDrawable().apply {
                color = Color.RED
                setBounds(root.right + dX.toInt(), root.top, root.right, root.bottom)
            }
        }
    }
}