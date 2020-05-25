package me.alfredobejarano.golfassistant.utils

import java.text.DecimalFormat
import kotlin.math.absoluteValue

fun Double?.asMoneyValue() = if (this == null) {
    "$0.00"
} else {
    val isNegative = this < 0.0
    val betAbsolute = this.absoluteValue.toString()
    val sign = if (isNegative) "-" else " "
    "$sign$${DecimalFormat("000,000,000.##").parse(betAbsolute)}"
}