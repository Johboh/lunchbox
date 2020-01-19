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
import com.fjun.lunchbox.database.State
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.lang.String.format

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val mainViewModel = ViewModelProviders.of(this)[MainViewModel::class.java]

        fab.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }

        val elseBoxesId: Short = 0
        val elseHeaderId: Short = 1
        val fridgeBoxesId: Short = 2
        val fridgeHeaderId: Short = 3
        val freezerBoxesId: Short = 4
        val freezerHeaderId: Short = 5

        val elseBoxAdapter = BoxesAdapter(this)
        val fridgeBoxAdapter = BoxesAdapter(this)
        val freezerBoxesAdapter = BoxesAdapter(this)

        val adapter =
            SectionedAdapter { fromPosition: Int, fromId: Short, toId: Short ->
                if (fromId == toId) return@SectionedAdapter

                val box = when (fromId) {
                    elseBoxesId -> elseBoxAdapter.getBox(fromPosition)
                    fridgeBoxesId -> fridgeBoxAdapter.getBox(fromPosition)
                    freezerBoxesId -> freezerBoxesAdapter.getBox(fromPosition)
                    else -> throw Exception(format("Unknown from ID %d", fromId));
                }

                val newState = when (toId) {
                    fridgeBoxesId, fridgeHeaderId -> State.FRIDGE
                    freezerBoxesId, freezerHeaderId -> State.FREEZER
                    else -> State.ELSE
                }

                // If we are transitioning from else to any other state, ask for food content.
                // Else only update if change in state (keeping timestamp)
                if (box.state == State.ELSE && (newState == State.FREEZER || newState == State.FRIDGE)) {
                    startActivity(EditActivity.createIntent(this, newState, box.content, box.uid))
                } else if (box.state != newState) {
                    GlobalScope.async {
                        mainViewModel.setState(box, newState)
                    }
                }
            }

        adapter.addAdapter(
            freezerHeaderId,
            HeaderAdapter(
                this,
                getString(R.string.header_title_freezer)
            )
        )
        adapter.addAdapter(freezerBoxesId, freezerBoxesAdapter)
        adapter.addAdapter(
            fridgeHeaderId,
            HeaderAdapter(
                this,
                getString(R.string.header_title_fridge)
            )
        )
        adapter.addAdapter(fridgeBoxesId, fridgeBoxAdapter)
        adapter.addAdapter(
            elseHeaderId,
            HeaderAdapter(
                this,
                getString(R.string.header_title_else)
            )
        )
        adapter.addAdapter(elseBoxesId, elseBoxAdapter)
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(this)

        val itemMoveCallback =
            ItemMoveCallback(adapter)
        val itemTouchHelper = ItemTouchHelper(itemMoveCallback)
        itemTouchHelper.attachToRecyclerView(list)

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
        mainViewModel.getBoxesWithState(State.ELSE).observe(this, Observer { boxes ->
            boxes?.let {
                elseBoxAdapter.setBoxes(boxes)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
