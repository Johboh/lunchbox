package com.fjun.lunchbox.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BoxDao {
    @Query("SELECT * FROM box")
    fun getAll(): List<Box>

    @Query("SELECT * FROM box WHERE state = (:state)")
    fun loadAllByState(state: Int): List<Box>

    @Insert
    fun insertAll(vararg boxes: Box)

    @Delete
    fun delete(box: Box)
}