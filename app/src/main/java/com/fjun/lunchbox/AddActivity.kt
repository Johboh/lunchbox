package com.fjun.lunchbox

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fjun.lunchbox.database.Box
import com.fjun.lunchbox.database.BoxDatabase
import com.fjun.lunchbox.database.State
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlin.random.Random

class AddActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        add_button.setOnClickListener { _ ->
            let {
                GlobalScope.async {
                    val state = State.values()[Random.nextInt(State.values().size)]
                    val box = Box(state, System.currentTimeMillis(), "ddd")
                    BoxDatabase.getDatabase(it.application).boxDao().insert(box)
                    it.finish()
                }
            }
        }
    }
}