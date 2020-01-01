package com.fjun.lunchbox

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.fjun.lunchbox.database.Box
import com.fjun.lunchbox.database.BoxDao
import com.fjun.lunchbox.database.BoxDatabase

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val boxDao: BoxDao = BoxDatabase.getDatabase(application).boxDao()

    fun getBoxesInFreezer(): LiveData<List<Box>> {
        return boxDao.loadAllByState(1)
    }

    fun getBoxesOutsideFreezer(): LiveData<List<Box>> {
        return boxDao.loadAllByState(2)
    }

    fun getAllBoxes(): LiveData<List<Box>> {
        return boxDao.getAll()
    }
}