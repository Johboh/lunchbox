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
    private var boxToUndo: Box? = null

    fun getBoxesWithState(state: State): LiveData<List<Box>> {
        return boxDao.getAllByState(state.state)
    }

    fun setState(box: Box, newState: State) = boxDao.setState(box.uid, newState)

    /**
     * Reset all values for the box to undo for the value in this box.
     */
    suspend fun undoBox() {
        val box: Box? = boxToUndo
        if (box != null) {
            // If box was deleted, insert. Else update/reset previous one
            if (boxDao.getSingleBox(box.uid) == null) {
                boxDao.insert(box)
            } else {
                boxDao.setContent(
                    box.uid,
                    box.state,
                    box.content ?: "",
                    box.timestampFreezer,
                    box.timestampFridge
                )
            }
        }
    }

    /**
     * Set or clear box to undo.
     */
    fun setBoxToUndo(box: Box?) {
        boxToUndo = box
    }

    fun hasBoxToUndo(): Boolean = boxToUndo != null
}