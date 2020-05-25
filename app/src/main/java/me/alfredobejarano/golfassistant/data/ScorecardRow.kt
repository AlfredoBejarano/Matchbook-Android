package me.alfredobejarano.golfassistant.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import me.alfredobejarano.golfassistant.utils.asMoneyValue
import java.text.DecimalFormat
import kotlin.math.absoluteValue

data class ScorecardRow(
    @Expose
    @SerializedName("bet")
    val bet: Double = 0.0,
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
    @SerializedName("result")
    val result: MatchResult = MatchResult.TIE,
    @Expose
    @SerializedName("date")
    val date: String = ""
) : Comparable<ScorecardRow> {
    override fun compareTo(other: ScorecardRow): Int {
        val contentsAreTheSame =
            other.bet == bet && other.match == match && other.order == order && other.handicap == handicap && other.date == date && other.result == result
        return if (contentsAreTheSame) {
            0
        } else {
            1
        }
    }

    fun getHandicapAsText() = handicap.toString()
    fun getBetAsText() = bet.asMoneyValue()
}