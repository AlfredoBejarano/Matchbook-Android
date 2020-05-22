package me.alfredobejarano.golfassistant.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.text.DecimalFormat

data class ScorecardRow(
    @Expose
    @SerializedName("bet")
    val bet: Bet = Bet(),
    @Expose
    @SerializedName("match")
    val match: String = "",
    @Expose
    @SerializedName("order")
    var order: Int = 0,
    @Expose
    @SerializedName("handicap")
    val handicap: Int = 0,
    @Expose
    @SerializedName("date")
    val date: String = ""
) : Comparable<ScorecardRow> {
    override fun compareTo(other: ScorecardRow): Int {
        val contentsAreTheSame =
            other.bet == bet && other.match == match && other.order == order && other.handicap == handicap && other.date == date
        return if (contentsAreTheSame) {
            0
        } else {
            1
        }
    }

    @Expose
    @SerializedName("total")
    val total = bet.earned - bet.lost

    fun isLoss() = total < 0
    fun getMatchText() = match.toString()
    fun getHandicapText() = handicap.toString()
    fun getWonEarnings() = "$${DecimalFormat("0000.##").parse(bet.earned.toString())}"
    fun getLossEarnings() = "$${DecimalFormat("0000.##").parse(bet.lost.toString())}"
    fun getTotalEarnings() = "$${DecimalFormat("0000.##").parse(total.toString())}"
}