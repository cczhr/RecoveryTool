package com.cczhr.recoverytool

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import com.cczhr.recoverytool.adapter.EditAdapter
import com.cczhr.recoverytool.database.AppDatabase
import com.cczhr.recoverytool.database.Command
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


/**
 * @author cczhr
 * @since  2020/9/6
 * @description https://github.com/cczhr
 */
class EditActivity : BaseActivity() {
    lateinit var editAdapter: EditAdapter
    override val layoutId: Int = R.layout.activity_edit
    var command: Command? = null

    @SuppressLint("SetTextI18n")
    override fun init() {
        supportActionBar?.setTitle(R.string.add)
        fabAnimate(fab)
        intent.getParcelableExtra<Command>("data")?.let {
            command = it
            supportActionBar?.setTitle(R.string.edit)
            name.postDelayed({
                name.setText(it.title)
            },1L)

            commands.postDelayed({
                commands.setText(it.commands)
            },1L)
        }

        common.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked)
                editAdapter.commonCommand()
        }
        twrp.setOnCheckedChangeListener { _, isChecked ->

            if (isChecked)
                editAdapter.twrpCommand()
        }


        editAdapter = EditAdapter(commands) { type, position, text ->
            val command = if (type == 1) "--$text" else text
            if (position == 0) {
                selectFile {
                    commands.append(if(type==1) "$command=$it\n" else "$command $it\n")
                }
            } else {
                commands.append("$command\n")
            }

        }


        val layoutManager = FlexboxLayoutManager(this)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = JustifyContent.FLEX_START
        recycler_view.layoutManager = layoutManager
        recycler_view.adapter = editAdapter
        recycler_view.itemAnimator = DefaultItemAnimator()
        editAdapter.commonCommand()

    }

    fun save(view: View) {
        val name = name.text.toString()
        val commands = commands.text.toString()
        if (name.isEmpty() || commands.isEmpty()) {
            Snackbar.make(view, R.string.input_cannot_be_empty, Snackbar.LENGTH_SHORT).show()
            return
        }
        GlobalScope.launch(Dispatchers.Main) {
            if (command != null) {
                command!!.title = name
                command!!.commands = commands
                AppDatabase.getDatabase(this@EditActivity).commandDao().update(command!!)
            } else {
                val newCommand = Command(name, commands)
                AppDatabase.getDatabase(this@EditActivity).commandDao().insert(newCommand)
            }
            finish()
        }

    }
}