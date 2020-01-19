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

/**
 * Edit the content of an existing lunch box.
 */
class EditActivity : AppCompatActivity() {

    private object Extras {
        const val STATE = "STATE"
        const val BOX_UID = "BOX_UID"
        const val CONTENT = "CONTENT"
    }

    private object Limits {
        const val CONTENT_LIMIT = 5
    }

    private var boxUid: Long = 0
    private var content: String = ""
    private var newState: State = State.UNUSED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        // Restore
        content = intent.getStringExtra(Extras.CONTENT) ?: ""
        boxUid =
            savedInstanceState?.getLong(Extras.BOX_UID) ?: intent.getLongExtra(Extras.BOX_UID, 0)
        newState = (savedInstanceState?.getSerializable(Extras.STATE)
            ?: intent.getSerializableExtra(Extras.STATE)) as State


        box_content_input.setText(content)

        // Setup list and adapters
        val viewModel = ViewModelProviders.of(this)[EditViewModel::class.java]
        val adapter = ContentAdapter(this) { content -> saveAndClose(viewModel, content) }
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(this)

        // List recent lunch box content
        viewModel.getRecentContent(Limits.CONTENT_LIMIT).observe(this, Observer { contents ->
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
        outState.putLong(Extras.BOX_UID, boxUid)
        outState.putSerializable(Extras.STATE, newState)
    }

    private fun saveAndClose(viewModel: EditViewModel, content: String) = let {
        // TODO figure out how to do this properly
        GlobalScope.async {
            viewModel.setContent(boxUid, newState, content)
            it.finish()
        }
    }

    companion object {
        fun createIntent(
            context: Context,
            newState: State,
            content: String?,
            boxUid: Long
        ): Intent {
            val intent = Intent(context, EditActivity::class.java)
            intent.putExtra(Extras.STATE, newState)
            intent.putExtra(Extras.BOX_UID, boxUid)
            intent.putExtra(Extras.CONTENT, content)
            return intent
        }
    }
}