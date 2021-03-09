package com.bda.finance_test.utils.string

import java.lang.StringBuilder

fun String.reverseToJsonString(): String {
    val builder = StringBuilder()
    var numCommas = 0
    var numChar = 0
    for (ch: Char in this) {
        if (ch == '[') {
            if (numChar == 0) {
                builder.append("{\"channel_id\":")
                numChar++
            }
        } else if (ch == ',' && numCommas == 0) {
            builder.append("$ch\"bid\":")
            numCommas++
        } else if (ch == ',' && numCommas == 1) {
            builder.append("$ch\"bid_size\":")
            numCommas++
        } else if (ch == ',' && numCommas == 2) {
            builder.append("$ch\"ask\":")
            numCommas++
        } else if (ch == ',' && numCommas == 3) {
            builder.append("$ch\"ask_size\":")
            numCommas++
        } else if (ch == ',' && numCommas == 4) {
            builder.append("$ch\"daily_change\":")
            numCommas++
        } else if (ch == ',' && numCommas == 5) {
            builder.append("$ch\"daily_change_perc\":")
            numCommas++
        } else if (ch == ',' && numCommas == 6) {
            builder.append("$ch\"last_price\":")
            numCommas++
        } else if (ch == ',' && numCommas == 7) {
            builder.append("$ch\"volume\":")
            numCommas++
        } else if (ch == ',' && numCommas == 8) {
            builder.append("$ch\"high\":")
            numCommas++
        } else if (ch == ',' && numCommas == 9) {
            builder.append("$ch\"low\":")
            numCommas++
        } else if (ch == ']') {
            builder.append("}")
        } else builder.append(ch)
    }
    val result = builder.toString()
    builder.clear()
    return result
}