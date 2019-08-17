package me.alfredobejarano.golfassistant.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Bet(
    @Expose
    @SerializedName("earned")
    val earned: Float,
    @Expose
    @SerializedName("lost")
    val lost: Float
)