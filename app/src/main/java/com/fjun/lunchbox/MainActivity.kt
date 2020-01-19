package com.fjun.lunchbox

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.fjun.lunchbox.adapter.BoxesAdapter
import com.fjun.lunchbox.adapter.HeaderAdapter
import com.fjun.lunchbox.adapter.ItemMoveCallback
import com.fjun.lunchbox.adapter.SectionedAdapter
import com.fjun.lunchbox.database.Box
import com.fjun.lunchbox.database.State
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.lang.String.format

/**
 * Starting Activity.
 */
class MainActivity : AppCompatActivity() {

    private object SectionIds {
        const val UNUSED_BOXES: Short = 0
        const val UNUSED_HEADER: Short = 1
        const val FRIDGE_BOXES: Short = 2
        const val FRIDGE_HEADER: Short = 3
        const val FREEZER_BOXES: Short = 4
        const val FREEZER_HEADER: Short = 5
    }

    private val onOverflowClick: (Box) -> Unit = {
        startActivity(EditActivity.createIntent(this, it.state, it.content, it.uid))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val mainViewModel = ViewModelProviders.of(this)[MainViewModel::class.java]

        fab.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }

        val unusedBoxAdapter = BoxesAdapter(this, onOverflowClick)
        val fridgeBoxAdapter = BoxesAdapter(this, onOverflowClick)
        val freezerBoxesAdapter = BoxesAdapter(this, onOverflowClick)

        // Sectioned Adapter with callback for when dropping boxes in other sections.
        val adapter =
            SectionedAdapter { fromPosition: Int, fromId: Short, toId: Short ->
                if (fromId == toId) return@SectionedAdapter

                // Get the box that was dropped
                val box = when (fromId) {
                    SectionIds.UNUSED_BOXES -> unusedBoxAdapter.getBox(fromPosition)
                    SectionIds.FRIDGE_BOXES -> fridgeBoxAdapter.getBox(fromPosition)
                    SectionIds.FREEZER_BOXES -> freezerBoxesAdapter.getBox(fromPosition)
                    else -> throw Exception(format("Unknown from ID %d", fromId));
                }
                mainViewModel.setBoxToUndo(box)

                // And figure out where it was dropped.
                val newState = when (toId) {
                    SectionIds.FRIDGE_BOXES, SectionIds.FRIDGE_HEADER -> State.FRIDGE
                    SectionIds.FREEZER_BOXES, SectionIds.FREEZER_HEADER -> State.FREEZER
                    else -> State.UNUSED
                }

                // If we are transitioning from unused to any other state, ask for food content.
                // Else only update if change in state (keeping timestamp)
                if (box.state == State.UNUSED && (newState == State.FREEZER || newState == State.FRIDGE)) {
                    startActivityForResult(
                        EditActivity.createIntent(
                            this,
                            newState,
                            box.content,
                            box.uid
                        ), 0
                    )
                } else if (box.state != newState) {
                    GlobalScope.async {
                        mainViewModel.setState(box, newState)
                        invalidateOptionsMenu()
                    }
                }
            }

        adapter.addAdapter(
            SectionIds.FREEZER_HEADER, HeaderAdapter(this, getString(R.string.header_title_freezer))
        )
        adapter.addAdapter(SectionIds.FREEZER_BOXES, freezerBoxesAdapter)
        adapter.addAdapter(
            SectionIds.FRIDGE_HEADER, HeaderAdapter(this, getString(R.string.header_title_fridge))
        )
        adapter.addAdapter(SectionIds.FRIDGE_BOXES, fridgeBoxAdapter)
        adapter.addAdapter(
            SectionIds.UNUSED_HEADER, HeaderAdapter(this, getString(R.string.header_title_unused))
        )
        adapter.addAdapter(SectionIds.UNUSED_BOXES, unusedBoxAdapter)
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(this)

        val itemMoveCallback = ItemMoveCallback(adapter)
        val itemTouchHelper = ItemTouchHelper(itemMoveCallback)
        itemTouchHelper.attachToRecyclerView(list)

        // Listen for changed to any box list.
        mainViewModel.getBoxesWithState(State.FREEZER).observe(this, Observer { boxes ->
            boxes?.let {
                freezerBoxesAdapter.setBoxes(boxes)
            }
        })
        mainViewModel.getBoxesWithState(State.FRIDGE).observe(this, Observer { boxes ->
            boxes?.let {
                fridgeBoxAdapter.setBoxes(boxes)
            }
        })
        mainViewModel.getBoxesWithState(State.UNUSED).observe(this, Observer { boxes ->
            boxes?.let {
                unusedBoxAdapter.setBoxes(boxes)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        invalidateOptionsMenu()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val mainViewModel = ViewModelProviders.of(this)[MainViewModel::class.java]
        return mainViewModel.hasBoxToUndo()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_undo) {
            val mainViewModel = ViewModelProviders.of(this)[MainViewModel::class.java]
            // TODO figure out how to do this
            GlobalScope.async {
                mainViewModel.undoBox()
                invalidateOptionsMenu()
                mainViewModel.setBoxToUndo(null)
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
