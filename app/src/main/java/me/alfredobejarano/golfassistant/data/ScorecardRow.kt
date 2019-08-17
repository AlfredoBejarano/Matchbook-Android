package me.alfredobejarano.golfassistant.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ScorecardRow(
    @Expose
    @SerializedName("bet")
    val bet: Bet = Bet(),
    @Expose
    @SerializedName("match")
    val match: Int = 0,
    @Expose
    @SerializedName("date")
    val date: String = "",
    @Expose
    @SerializedName("handicap")
    val handicap: Int = 0
) {
    @Expose
    @SerializedName("total")
    val total = bet.earned - bet.lost
}