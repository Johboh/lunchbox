package com.fjun.lunchbox

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.fjun.lunchbox.database.Box
import com.fjun.lunchbox.database.BoxDao
import com.fjun.lunchbox.database.BoxDatabase
import com.fjun.lunchbox.database.State

/**
 * View model for the Main Activity.
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val boxDao: BoxDao = BoxDatabase.getDatabase(application).boxDao()

    fun getBoxesWithState(state: State): LiveData<List<Box>> {
        return boxDao.getAllByState(state.state)
    }

    fun setState(box: Box, newState: State) {
        boxDao.setState(box.uid, newState, System.currentTimeMillis())
    }
}