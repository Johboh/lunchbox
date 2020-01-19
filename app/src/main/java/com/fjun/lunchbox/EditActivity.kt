package com.fjun.lunchbox

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fjun.lunchbox.database.BoxDatabase
import com.fjun.lunchbox.database.State
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class EditActivity : AppCompatActivity() {

    var boxUid: Long = 0
    var newState: State = State.ELSE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        boxUid = savedInstanceState?.getLong("BOX_UID") ?: intent.getLongExtra("BOX_UID", 0)
        newState = (savedInstanceState?.getSerializable("STATE")
            ?: intent.getSerializableExtra("STATE")) as State

        save_button.setOnClickListener { _ ->
            let {
                GlobalScope.async {
                    BoxDatabase.getDatabase(it.application).boxDao().setContent(
                        boxUid,
                        newState,
                        box_content_input.text.toString(),
                        System.currentTimeMillis()
                    )
                    it.finish()
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong("BOX_UID", boxUid)
    }

    companion object {
        fun createIntent(context: Context, newState: State, boxUid: Long): Intent {
            val intent = Intent(context, EditActivity::class.java)
            intent.putExtra("BOX_UID", boxUid)
            intent.putExtra("STATE", newState)
            return intent
        }
    }
}