package com.fjun.lunchbox

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.fjun.lunchbox.database.State
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { _ ->
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }

        val freezeHeaderId: Short = 0;
        val elseHeaderId: Short = 1;

        val adapter = SectionedAdapter()
        val freezeBoxesAdapter = BoxesAdapter(this)
        val elseBoxAdapter = BoxesAdapter(this)
        adapter.addAdapter(freezeHeaderId, HeaderAdapter(this, "Freezer"))
        adapter.addAdapter(10, freezeBoxesAdapter)
        adapter.addAdapter(elseHeaderId, HeaderAdapter(this, "Else"))
        adapter.addAdapter(20, elseBoxAdapter)
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(this)

        val mainViewModel = ViewModelProviders.of(this)[MainViewModel::class.java]
        mainViewModel.getBoxesWithState(State.FREEZER).observe(this, Observer { boxes ->
            boxes?.let {
                freezeBoxesAdapter.setBoxes(boxes)
                adapter.showAdapter(freezeHeaderId, boxes.isNotEmpty())
            }
        })
        mainViewModel.getBoxesWithoutState(State.FREEZER).observe(this, Observer { boxes ->
            boxes?.let {
                elseBoxAdapter.setBoxes(boxes)
                adapter.showAdapter(elseHeaderId, boxes.isNotEmpty())
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
