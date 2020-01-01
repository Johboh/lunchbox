package com.fjun.lunchbox.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "boxes")
data class Box(
    @ColumnInfo(name = "state") val state: State,
    @ColumnInfo(name = "timestamp") val timestamp: Long,
    @ColumnInfo(name = "content") val content: String?
) {
    @PrimaryKey(autoGenerate = true)
    var uid: Long = 0
}