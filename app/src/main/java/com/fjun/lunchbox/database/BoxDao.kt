package com.fjun.lunchbox.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BoxDao {
    @Query("SELECT * FROM boxes ORDER BY timestamp DESC")
    fun getAll(): LiveData<List<Box>>

    @Query("SELECT * FROM boxes WHERE state = (:state) ORDER BY timestamp DESC")
    fun loadAllByState(state: Int): LiveData<List<Box>>

    @Insert
    suspend fun insert(boxes: Box)

    @Delete
    suspend fun delete(box: Box)
}