package me.alfredobejarano.golfassistant.utils

import android.text.Editable

fun Editable?.toFloat() = this?.toString()?.toFloatOrNull() ?: 0f