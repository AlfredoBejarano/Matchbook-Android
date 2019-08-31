package me.alfredobejarano.golfassistant.adapters

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

abstract class SwipeToDeleteCallback(private val ctx: Context) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    override fun onMove(rv: RecyclerView, vh: RecyclerView.ViewHolder, t: RecyclerView.ViewHolder) =
        false

    override fun onChildDraw(
        canvas: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
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