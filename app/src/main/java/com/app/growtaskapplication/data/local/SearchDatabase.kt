package com.app.growtaskapplication.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.app.growtaskapplication.data.model.Item

@Database(entities = [Item::class], version = 4)
@TypeConverters(Converters::class)
abstract class SearchDatabase : RoomDatabase() {
    abstract fun searchDao():SearchDao
}