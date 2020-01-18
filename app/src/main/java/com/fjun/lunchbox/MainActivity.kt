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
        val freezeBoxesId: Short = 2
        val freezeHeaderId: Short = 3

        val freezeBoxesAdapter = BoxesAdapter(this)
        val elseBoxAdapter = BoxesAdapter(this)

        val adapter = SectionedAdapter { fromPosition: Int, fromId: Short, toId: Short ->
            if (fromId == toId) return@SectionedAdapter

            val box = when (fromId) {
                freezeBoxesId -> freezeBoxesAdapter.getBox(fromPosition)
                elseBoxesId -> elseBoxAdapter.getBox(fromPosition)
                else -> throw Exception(format("Uknown from ID %d", fromId));
            }

            GlobalScope.async {
                mainViewModel.setState(
                    box,
                    if (toId == freezeBoxesId || toId == freezeHeaderId) State.FREEZER else State.ELSE
                )
            }
        }

        adapter.addAdapter(freezeHeaderId, HeaderAdapter(this, "Freezer"))
        adapter.addAdapter(freezeBoxesId, freezeBoxesAdapter)
        adapter.addAdapter(elseHeaderId, HeaderAdapter(this, "Else"))
        adapter.addAdapter(elseBoxesId, elseBoxAdapter)
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(this)

        val itemMoveCallback = ItemMoveCallback(adapter)
        val itemTouchHelper = ItemTouchHelper(itemMoveCallback)
        itemTouchHelper.attachToRecyclerView(list)

        mainViewModel.getBoxesWithState(State.FREEZER).observe(this, Observer { boxes ->
            boxes?.let {
                freezeBoxesAdapter.setBoxes(boxes)
            }
        })
        mainViewModel.getBoxesWithoutState(State.FREEZER).observe(this, Observer { boxes ->
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
