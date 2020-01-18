package com.fjun.lunchbox.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Box::class], version = 3)
@TypeConverters(Converters::class)
abstract class BoxDatabase : RoomDatabase() {
    abstract fun boxDao(): BoxDao

    companion object {

        private val MIGRATION_TEMP = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
            }
        }

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
                ).addMigrations(MIGRATION_TEMP).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}