package com.bda.finance_test.model.models

import com.google.gson.annotations.SerializedName

data class TickerData (
    @SerializedName("channel_id")
    var channelId: Long = 0,
    @SerializedName("bid")
    var bid: Double = 0.0,
    @SerializedName("bid_size")
    var bidSize: Double = 0.0,
    @SerializedName("ask")
    var ask: Double = 0.0,
    @SerializedName("ask_size")
    var askSize: Double = 0.0,
    @SerializedName("daily_change")
    var dailyChange: Double = 0.0,
    @SerializedName("daily_change_perc")
    var dailyChangePerc: Double = 0.0,
    @SerializedName("last_price")
    var lastPrice: Double = 0.0,
    @SerializedName("volume")
    var volume: Double = 0.0,
    @SerializedName("high")
    var high: Double = 0.0,
    @SerializedName("low")
    var low: Double = 0.0
)