package com.fjun.lunchbox.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * A Lunch box.
 */
@Entity(tableName = "boxes")
data class Box(
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "state") val state: State,
    @ColumnInfo(name = "timestamp_freezer") val timestampFreezer: Long,
    @ColumnInfo(name = "timestamp_fridge") val timestampFridge: Long,
    @ColumnInfo(name = "content") val content: String?
) {
    @PrimaryKey(autoGenerate = true)
    var uid: Long = 0
}