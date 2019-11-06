package me.alfredobejarano.golfassistant.utils

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter

abstract class BindingAdapter {
    companion object {
        @JvmStatic
        @BindingAdapter("earnings")
        fun setEarnings(field: TextView, earnings: String) {
            field.text = earnings
            field.visibility = if (earnings.length <= 3 && earnings.last() == '0') {
                View.GONE
            } else {
                View.VISIBLE
            }
        }
    }
}