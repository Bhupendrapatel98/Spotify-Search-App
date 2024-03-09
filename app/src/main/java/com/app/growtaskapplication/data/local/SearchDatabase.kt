package com.app.growtaskapplication.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.app.growtaskapplication.data.model.Item

@Database(entities = [Item::class], version = 1)
abstract class SearchDatabase : RoomDatabase() {
    abstract fun searchDao():SearchDao
}