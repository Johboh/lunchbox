package com.fjun.lunchbox.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BoxDao {
    @Query("SELECT * FROM boxes")
    fun getAll(): LiveData<List<Box>>

    @Query("SELECT * FROM boxes WHERE state = (:state)")
    fun loadAllByState(state: Int): LiveData<List<Box>>

    @Insert
    fun insertAll(vararg boxes: Box)

    @Delete
    fun delete(box: Box)
}