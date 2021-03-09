package com.bda.finance_test.ui.currencyPairs

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.bda.finance_test.model.database.entity.CurrencyPair
import com.bda.finance_test.model.repository.PairRepository
import com.bda.finance_test.utils.coroutine.getScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class CurrencyPairsViewModel(
    applicationContext: Application,
    private val pairRepository: PairRepository
) : AndroidViewModel(applicationContext) {

    companion object {
        const val ALL_PAIRS: Int = 0
        const val FAVORITES_PAIRS: Int = 1
    }

    private var viewModelJob = SupervisorJob()
    private var uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val fragmentType = MutableLiveData<Int>().apply {
        value = 0
    }

    private val _pairsList = MutableLiveData<List<CurrencyPair>?>().apply {
        uiScope.getScope().launch {
            val templatePairList: MutableList<CurrencyPair>? =
                pairRepository.allPairs() as MutableList<CurrencyPair>
            if (templatePairList != null && templatePairList.isEmpty()) {
                templatePairList.add(CurrencyPair(0, "BTC/USD", "btcusd"))
                templatePairList.add(CurrencyPair(0, "ETH/USD", "ethusd"))
                templatePairList.add(CurrencyPair(0, "SUSHI/USD", "sushi:usd"))
                templatePairList.add(CurrencyPair(0, "UNI/USD", "uniusd"))
                templatePairList.add(CurrencyPair(0, "DOT/USD", "dotusd"))
                templatePairList.add(CurrencyPair(0, "XRP/USD", "xrpusd"))
                templatePairList.add(CurrencyPair(0, "ADA/USD", "adausd"))
                templatePairList.add(CurrencyPair(0, "LTC/USD", "ltcusd"))
                templatePairList.add(CurrencyPair(0, "IOTA/USD", "iotusd"))
                templatePairList.add(CurrencyPair(0, "LINK/USD", "link:usd"))
                templatePairList.add(CurrencyPair(0, "DASH/USD", "dshusd"))
                templatePairList.add(CurrencyPair(0, "NEO/USD", "neousd"))
                templatePairList.add(CurrencyPair(0, "FIL/USD", "filusd"))
                templatePairList.add(CurrencyPair(0, "XTZ/USD", "xtzusd"))
                templatePairList.add(CurrencyPair(0, "BAT/USD", "batusd"))
                templatePairList.add(CurrencyPair(0, "TRX/USD", "trxusd"))
                templatePairList.add(CurrencyPair(0, "MANA/USD", "mnausd"))
                templatePairList.add(CurrencyPair(0, "EGLD/USD", "egld:usd"))
                templatePairList.add(CurrencyPair(0, "YFI/USD", "yfiusd"))
                templatePairList.add(CurrencyPair(0, "ENJ/USD", "enjusd"))
                templatePairList.add(CurrencyPair(0, "COMP/USD", "comp:usd"))
                templatePairList.add(CurrencyPair(0, "ALGO/USD", "algusd"))
                templatePairList.add(CurrencyPair(0, "OMG/USD", "omgusd"))
                templatePairList.add(CurrencyPair(0, "ZEC/USD", "zecusd"))
                templatePairList.add(CurrencyPair(0, "VET/USD", "vetusd"))
                templatePairList.add(CurrencyPair(0, "AAVE/USD", "aave:usd"))
                templatePairList.add(CurrencyPair(0, "ATOM/USD", "atousd"))
                templatePairList.add(CurrencyPair(0, "XLM/USD", "xlmusd"))
                templatePairList.add(CurrencyPair(0, "ZRX/USD", "zrxusd"))
                templatePairList.add(CurrencyPair(0, "BCHN/USD", "bchn:usd"))
                templatePairList.add(CurrencyPair(0, "XMR/USD", "xmrusd"))
                templatePairList.add(CurrencyPair(0, "BSV/USD", "bsvusd"))
                templatePairList.add(CurrencyPair(0, "USDC/USD", "udcusd"))
                templatePairList.add(CurrencyPair(0, "ZIL/USD", "zilusd"))
                templatePairList.add(CurrencyPair(0, "DAI/USD", "daiusd"))
                pairRepository.insertList(templatePairList)
                value = if (fragmentType.value == ALL_PAIRS) pairRepository.allPairs()
                else pairRepository.getFavorites()
            } else {
                value = if (fragmentType.value == ALL_PAIRS) pairRepository.allPairs()
                else pairRepository.getFavorites()
            }
        }
    }
    val pairsList: MutableLiveData<List<CurrencyPair>?>
        get() = _pairsList

    fun updatePairList() {
        uiScope.getScope().launch {
            _pairsList.apply {
                value = if (fragmentType.value == ALL_PAIRS) pairRepository.allPairs()
                else pairRepository.getFavorites()
            }
        }
    }

    fun searchPair(search: String) {
        uiScope.getScope().launch {
            _pairsList.apply {
                value = pairRepository.searchPair("%$search%")
            }
        }
    }

    fun updatePair(pair: CurrencyPair) {
        uiScope.getScope().launch {
            _pairsList.apply {
                pairRepository.update(pair = pair)
                value = if (fragmentType.value == ALL_PAIRS) pairRepository.allPairs()
                else pairRepository.getFavorites()
            }
        }

    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
