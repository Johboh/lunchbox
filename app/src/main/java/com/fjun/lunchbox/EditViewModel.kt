package com.fjun.lunchbox

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.fjun.lunchbox.database.Box
import com.fjun.lunchbox.database.BoxDao
import com.fjun.lunchbox.database.BoxDatabase
import com.fjun.lunchbox.database.State

/**
 * View model for the Edit Activity.
 */
class EditViewModel(application: Application) : AndroidViewModel(application) {
    private val boxDao: BoxDao = BoxDatabase.getDatabase(application).boxDao()

    fun getRecentContent(limit: Int): LiveData<List<String>> = boxDao.getRecentContent(limit)

    /**
     * Set content, but keep current state and timestamp.
     */
    fun setContent(boxUid: Long, content: String) = boxDao.setContent(boxUid, content)

    /**
     * Set content and update state and timestamp.
     */
    fun setContent(boxUid: Long, newState: State, content: String) =
        boxDao.setContent(boxUid, newState, content, System.currentTimeMillis())

    fun getBox(boxUid: Long): Box? = boxDao.getSingleBox(boxUid)

    fun delete(boxUid: Long) = boxDao.delete(boxUid)
}