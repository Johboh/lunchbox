package com.fjun.lunchbox

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.fjun.lunchbox.adapter.ContentAdapter
import com.fjun.lunchbox.database.State
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class EditActivity : AppCompatActivity() {

    var boxUid: Long = 0
    var content : String = ""
    var newState: State = State.ELSE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        content = intent.getStringExtra("CONTENT") ?: ""
        boxUid = savedInstanceState?.getLong("BOX_UID") ?: intent.getLongExtra("BOX_UID", 0)
        newState = (savedInstanceState?.getSerializable("STATE")
            ?: intent.getSerializableExtra("STATE")) as State

        val viewModel = ViewModelProviders.of(this)[EditViewModel::class.java]

        box_content_input.setText(content)

        val adapter = ContentAdapter(this) { content -> saveAndClose(viewModel, content) }
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(this)

        viewModel.getRecentContent(5).observe(this, Observer { contents ->
            contents?.let {
                adapter.setContents(contents)
            }
        })

        save_button.setOnClickListener { _ ->
            saveAndClose(
                viewModel,
                box_content_input.text.toString()
            )
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong("BOX_UID", boxUid)
    }

    private fun saveAndClose(viewModel: EditViewModel, content: String) = let {
        GlobalScope.async {
            viewModel.setContent(boxUid, newState, content)
            it.finish()
        }
    }

    companion object {
        fun createIntent(context: Context, newState: State, content : String?, boxUid: Long): Intent {
            val intent = Intent(context, EditActivity::class.java)
            intent.putExtra("BOX_UID", boxUid)
            intent.putExtra("STATE", newState)
            intent.putExtra("CONTENT", content)
            return intent
        }
    }
}