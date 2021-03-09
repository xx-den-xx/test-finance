package com.bda.finance_test.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bda.finance_test.model.database.dao.CurrencyPairDao
import com.bda.finance_test.model.database.entity.CurrencyPair

@Database(entities = [CurrencyPair::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract val currencyPairDao: CurrencyPairDao
}