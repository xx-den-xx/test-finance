package com.bda.finance_test.model.database.dao

import androidx.room.*
import com.bda.finance_test.model.database.entity.CurrencyPair

@Dao
interface CurrencyPairDao {

    @Query("SELECT * FROM pairs")
    suspend fun findAll(): List<CurrencyPair>

    @Query("SELECT * FROM pairs WHERE favorite = 1")
    suspend fun getFavorites(): List<CurrencyPair>

    @Query("SELECT * FROM pairs WHERE title LIKE :search")
    suspend fun searchPairs(search: String): List<CurrencyPair>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(pairs: List<CurrencyPair>): Array<Long>

    @Update
    suspend fun update(pair: CurrencyPair)
}