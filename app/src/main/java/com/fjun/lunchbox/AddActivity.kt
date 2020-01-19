package com.fjun.lunchbox

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fjun.lunchbox.database.Box
import com.fjun.lunchbox.database.BoxDatabase
import com.fjun.lunchbox.database.State
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class AddActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        add_button.setOnClickListener { _ ->
            let {
                GlobalScope.async {
                    val box = Box(
                        box_name_input.text.toString(),
                        State.ELSE,
                        System.currentTimeMillis(),
                        box_content_input.text.toString()
                    )
                    BoxDatabase.getDatabase(it.application).boxDao().insert(box)
                    it.finish()
                }
            }
        }
    }
}