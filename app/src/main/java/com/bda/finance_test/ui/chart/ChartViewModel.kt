package com.bda.finance_test.ui.chart

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.bda.finance_test.model.database.entity.CurrencyPair
import com.bda.finance_test.model.models.TickerData
import com.bda.finance_test.model.repository.WebSocketRepository
import com.bda.finance_test.utils.coroutine.getScope
import com.bda.finance_test.utils.string.reverseToJsonString
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch


class ChartViewModel(
    application: Application,
    private val repository: WebSocketRepository
) : AndroidViewModel(application) {

    var currencyPair: MutableLiveData<CurrencyPair> = MutableLiveData<CurrencyPair>()

    private var viewModelJob = SupervisorJob()
    private var uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _tickerData: MutableLiveData<TickerData> = MutableLiveData<TickerData>().apply {
        uiScope.getScope().launch {
            repository.message = currencyPair.value!!.symbol
            repository.startWebSocket {
                var numberSquareBrackets = 0
                it.forEach {ch ->
                    if (ch == '[' || ch == ']') {
                        numberSquareBrackets++
                    }
                }

                if (numberSquareBrackets in 1..2) {
                    val ticker: TickerData?
                    try {
                        ticker = GsonBuilder().create().fromJson(it.reverseToJsonString(), TickerData::class.java)
                        postValue(ticker)
                    } catch (ex: NumberFormatException) {
                        postValue(null)
                    }
                }
            }
        }
    }
    val tickerData: MutableLiveData<TickerData>
        get() = _tickerData



    override fun onCleared() {
        super.onCleared()
        repository.closeWebSocket()
    }
}