package com.fjun.lunchbox.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Box::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun boxDao(): BoxDao
}