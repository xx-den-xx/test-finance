package com.bda.finance_test.model.database.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "pairs")
data class CurrencyPair(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val title: String,
    val symbol: String,
    var favorite: Int = 0
) : Parcelable{

    fun setFavorite(favorite: Boolean) {
        this.favorite = if (favorite) 1 else 0
    }

    fun isFavorite(): Boolean {
        return favorite == 1
    }
}
