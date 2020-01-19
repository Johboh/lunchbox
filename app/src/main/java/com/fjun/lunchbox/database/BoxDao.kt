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
    fun getAllByState(state: Int): LiveData<List<Box>>

    @Query("SELECT * FROM boxes WHERE state != (:state) ORDER BY timestamp DESC")
    fun getAllWithoutState(state: Int): LiveData<List<Box>>

    @Query("UPDATE boxes SET state = (:newState), timestamp = (:timestamp) WHERE uid = (:boxUid)")
    fun setState(boxUid: Long, newState: State, timestamp: Long)

    @Query("UPDATE boxes SET state = (:newState), content = (:content), timestamp = (:timestamp) WHERE uid = (:boxUid)")
    fun setContent(boxUid: Long, newState: State, content : String, timestamp: Long)

    @Insert
    suspend fun insert(boxes: Box)

    @Delete
    suspend fun delete(box: Box)
}