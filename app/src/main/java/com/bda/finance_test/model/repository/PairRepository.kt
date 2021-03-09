package com.bda.finance_test.model.repository

import android.util.Log
import com.bda.finance_test.model.database.dao.CurrencyPairDao
import com.bda.finance_test.model.database.entity.CurrencyPair

class PairRepository(private val pairDao: CurrencyPairDao) {

    suspend fun allPairs(): List<CurrencyPair> {
        val list = pairDao.findAll()
        Log.e("PairRepository", "allPairs: list size = ${list.size}")
        return list
    }

    suspend fun getFavorites(): List<CurrencyPair> = pairDao.getFavorites()

    suspend fun searchPair(search: String) = pairDao.searchPairs(search = search)

    suspend fun insertList(pairs: List<CurrencyPair>): Array<Long> { return pairDao.insertList(pairs = pairs) }

    suspend fun update(pair: CurrencyPair) { pairDao.update(pair = pair)}

    //suspend fun insert(pair: CurrencyPair) { pairDao.insert(pair = pair) }
}