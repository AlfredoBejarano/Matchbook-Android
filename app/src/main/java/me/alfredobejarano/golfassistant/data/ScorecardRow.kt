package me.alfredobejarano.golfassistant.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ScorecardRow(
    @Expose
    @SerializedName("bet")
    val bet: Bet,
    @Expose
    @SerializedName("match")
    val match: Int,
    @Expose
    @SerializedName("date")
    val date: String,
    @Expose
    @SerializedName("handicap")
    val handicap: Int
) {
    @Expose
    @SerializedName("total")
    val total = bet.earned - bet.lost
}