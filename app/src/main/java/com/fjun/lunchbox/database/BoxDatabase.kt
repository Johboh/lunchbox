package com.fjun.lunchbox.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Box::class], version = 4)
@TypeConverters(Converters::class)
abstract class BoxDatabase : RoomDatabase() {
    abstract fun boxDao(): BoxDao

    companion object {

        private val MIGRATION_TEMP = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE boxes_new (name TEXT NOT NULL, timestamp_fridge INTEGER NOT NULL, uid INTEGER NOT NULL, state INTEGER NOT NULL, timestamp_freezer INTEGER NOT NULL, content TEXT, PRIMARY KEY(uid))")
                database.execSQL("INSERT INTO boxes_new (name, timestamp_freezer, timestamp_fridge, state, content) SELECT name, timestamp, '0', state, content FROM boxes")
                database.execSQL("DROP TABLE boxes")
                database.execSQL("ALTER TABLE boxes_new RENAME TO boxes")
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