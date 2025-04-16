package com.ajdev.vnzlaapp.ui.utils

fun Double.formatWithSymbol(symbol: String): String {
    return "$symbol ${"%.2f".format(this)}"
}