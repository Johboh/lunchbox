package com.fjun.lunchbox.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BoxDao {
    @Query("SELECT * FROM boxes WHERE uid = (:boxUid)")
    fun getSingleBox(boxUid: Long): Box?

    @Query("SELECT * FROM boxes WHERE state = (:state) ORDER BY timestamp DESC")
    fun getAllByState(state: Int): LiveData<List<Box>>

    @Query("SELECT DISTINCT content FROM boxes WHERE content != \"\" ORDER BY timestamp DESC LIMIT (:limit)")
    fun getRecentContent(limit: Int): LiveData<List<String>>

    @Query("UPDATE boxes SET state = (:newState), timestamp = (:timestamp) WHERE uid = (:boxUid)")
    fun setState(boxUid: Long, newState: State, timestamp: Long)

    @Query("UPDATE boxes SET content = (:content) WHERE uid = (:boxUid)")
    fun setContent(boxUid: Long, content: String)

    @Query("UPDATE boxes SET state = (:newState), content = (:content), timestamp = (:timestamp) WHERE uid = (:boxUid)")
    fun setContent(boxUid: Long, newState: State, content: String, timestamp: Long)

    @Insert
    suspend fun insert(box: Box)

    @Delete
    suspend fun delete(box: Box)

    @Query("DELETE FROM boxes WHERE uid = (:boxUid)")
    fun delete(boxUid: Long)
}