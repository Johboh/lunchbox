package com.fjun.lunchbox.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Box::class], version = 1)
abstract class BoxDatabase : RoomDatabase() {
    abstract fun boxDao(): BoxDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: BoxDatabase? = null

        fun getDatabase(context: Context): BoxDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BoxDatabase::class.java,
                    "box_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}