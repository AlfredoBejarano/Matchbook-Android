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
        }

        @JvmStatic
        @BindingAdapter("android:visibility")
        fun setVisibility(view: View, visible: Boolean) {
            view.visibility = if (visible) View.VISIBLE else View.INVISIBLE
        }
    }
}