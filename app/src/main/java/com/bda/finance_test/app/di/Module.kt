package com.bda.finance_test.app.di

import android.app.Application
import androidx.room.Room
import com.bda.finance_test.model.database.AppDatabase
import com.bda.finance_test.model.database.dao.CurrencyPairDao
import com.bda.finance_test.model.repository.PairRepository
import com.bda.finance_test.model.repository.WebSocketRepository
import com.bda.finance_test.ui.chart.ChartViewModel
import com.bda.finance_test.ui.currencyPairs.CurrencyPairsViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import java.net.URI

val viewModelModule = module {
    single { CurrencyPairsViewModel(get(), get()) }
    factory { ChartViewModel(get(), get())}
}

val databaseModule = module {
    fun provideDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, "database")
            .fallbackToDestructiveMigration()
            .build()
    }

    fun provideDao(database: AppDatabase): CurrencyPairDao {
        return database.currencyPairDao
    }

    single { provideDatabase(androidApplication())}
    single { provideDao(get())}
}

val pairRepositoryModule = module {
    fun providePairRepository(dao: CurrencyPairDao) : PairRepository {
        return PairRepository(pairDao = dao)
    }

    single { providePairRepository(get()) }
}

val webSocketModule = module {
    fun provideWebSocketRepository(uri: URI): WebSocketRepository {
        return WebSocketRepository(uri)
    }

    fun provideUri() : URI {
        return URI("wss://api.bitfinex.com/ws")
    }

    single { provideWebSocketRepository(get()) }
    single { provideUri() }
}