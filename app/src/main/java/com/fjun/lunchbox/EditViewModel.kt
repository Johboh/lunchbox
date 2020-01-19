package com.fjun.lunchbox

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.fjun.lunchbox.database.BoxDao
import com.fjun.lunchbox.database.BoxDatabase
import com.fjun.lunchbox.database.State

/**
 * View model for the Edit Activity.
 */
class EditViewModel(application: Application) : AndroidViewModel(application) {
    private val boxDao: BoxDao = BoxDatabase.getDatabase(application).boxDao()

    fun getRecentContent(limit: Int): LiveData<List<String>> {
        return boxDao.getRecentContent(limit)
    }

    fun setContent(boxUid: Long, newState: State, content: String) {
        boxDao.setContent(boxUid, newState, content, System.currentTimeMillis())
    }
}