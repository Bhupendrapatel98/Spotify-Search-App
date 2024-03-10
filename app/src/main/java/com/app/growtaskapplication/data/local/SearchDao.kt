package com.app.growtaskapplication.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.growtaskapplication.data.model.Item
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchDao {

    @Query("SELECT * FROM searchResults")
    fun getAllSearch(): List<Item>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearch(searchItem : List<Item>)

    @Query("DELETE FROM searchResults")
    suspend fun clearData()
}