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

    @Query("SELECT * FROM boxes WHERE state = (:state) ORDER BY timestamp_freezer, timestamp_fridge DESC")
    fun getAllByState(state: Int): LiveData<List<Box>>

    @Query("SELECT DISTINCT content FROM boxes WHERE content != \"\" ORDER BY timestamp_freezer, timestamp_fridge DESC LIMIT (:limit)")
    fun getRecentContent(limit: Int): LiveData<List<String>>

    @Query("UPDATE boxes SET state = (:state), timestamp_freezer = (:timestampFreezer) WHERE uid = (:boxUid)")
    fun setInFreezer(boxUid: Long, state: State, timestampFreezer: Long)

    @Query("UPDATE boxes SET state = (:state), timestamp_fridge = (:timestampFridge) WHERE uid = (:boxUid)")
    fun setInFridge(boxUid: Long, state: State, timestampFridge: Long)

    @Query("UPDATE boxes SET state = (:state) WHERE uid = (:boxUid)")
    fun setState(boxUid: Long, state: State)

    @Query("UPDATE boxes SET content = (:content) WHERE uid = (:boxUid)")
    fun setContent(boxUid: Long, content: String)

    @Query("UPDATE boxes SET state = (:newState), content = (:content), timestamp_freezer = (:timestampFreezer), timestamp_fridge = (:timestampFridge) WHERE uid = (:boxUid)")
    fun setContent(
        boxUid: Long,
        newState: State,
        content: String,
        timestampFreezer: Long,
        timestampFridge: Long
    )

    @Insert
    suspend fun insert(box: Box)

    @Delete
    suspend fun delete(box: Box)

    @Query("DELETE FROM boxes WHERE uid = (:boxUid)")
    fun delete(boxUid: Long)
}