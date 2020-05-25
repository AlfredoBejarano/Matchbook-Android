package me.alfredobejarano.golfassistant.utils

import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import me.alfredobejarano.golfassistant.R
import me.alfredobejarano.golfassistant.data.MatchResult

abstract class BindingAdapter {
    companion object {
        @JvmStatic
        @BindingAdapter("match_result")
        fun setEarnings(field: TextView, matchResult: MatchResult) {
            val resultDrawable = ContextCompat.getDrawable(
                field.context, when (matchResult) {
                    MatchResult.TIE -> R.drawable.ic_tie_amber_24dp
                    MatchResult.WIN -> R.drawable.ic_arrow_upward_green_24dp
                    MatchResult.LOSS -> R.drawable.ic_arrow_downward_red_24dp
                }
            )

            field.setCompoundDrawablesRelativeWithIntrinsicBounds(resultDrawable, null, null, null)
        }

    }
}