package com.cczhr.recoverytool

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import com.cczhr.recoverytool.adapter.MainAdapter
import com.cczhr.recoverytool.database.AppDatabase
import com.cczhr.recoverytool.database.Command
import com.cczhr.recoverytool.utils.COMMAND_FILE
import com.cczhr.recoverytool.utils.TWRP_FILE
import com.cczhr.recoverytool.utils.rebootRecovery
import com.cczhr.recoverytool.utils.writeAndRestartRecovery
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * @author cczhr
 * @since  2020/9/6
 * @description https://github.com/cczhr
 */
class MainActivity : BaseActivity() {
    override val layoutId: Int = R.layout.main
    lateinit var adapter: MainAdapter
    private var commandList = ArrayList<Command>()
    override fun init() {
        adapter = MainAdapter(commandList, this@MainActivity)
        recycler_view.adapter = adapter
        ItemTouchHelper(MainAdapter.CardItemTouchHelperCallback(adapter)).attachToRecyclerView(recycler_view)
        refresh()
        requestPermissions {
        }
        bar.replaceMenu(R.menu.bar_menu)
        bar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.about -> {
                    about()
                    true
                }
                R.id.reboot_recovery -> {
                    rebootRecovery()
                    true
                }
                R.id.delete -> {
                    deleteSelectItem()
                    true
                }
                R.id.add -> {
                    addItem()
                    true
                }
                R.id.show_commands -> {
                    showCommands()
                    true
                }

                else -> true
            }
        }
        fabAnimate(fab)

    }

    fun runCommand(view: View) {
        val commands = adapter.getSelectCommands()
        if (commands.isNotEmpty())
            MaterialAlertDialogBuilder(this)
                .setTitle(R.string.command)
                .setMessage(commands)
                .setPositiveButton(R.string.twrp) { dialog, _ ->
                    dialog.dismiss()
                    writeAndRestartRecovery(this, TWRP_FILE,commands)
                }
                .setNegativeButton(R.string.common) { dialog, _ ->
                    dialog.dismiss()
                    writeAndRestartRecovery(this,COMMAND_FILE,commands)

                }
                .setNeutralButton(R.string.cancel, null).show()
    }


    private fun about() {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.source_code)
            .setView(R.layout.dialog_about)
            .show()
    }


    private fun deleteSelectItem() {
        GlobalScope.launch( Dispatchers.Main) {
           val list= adapter.getSelectItem()
            if(adapter.getSelectItem().isNotEmpty()){
                AppDatabase.getDatabase(this@MainActivity).commandDao().delete(list)
                refresh()
            }


        }

    }


    private fun refresh(){
        GlobalScope.launch( Dispatchers.Main) {
            commandList.clear()
            commandList.addAll(AppDatabase.getDatabase(this@MainActivity).commandDao().getAll())
            adapter.notifyDataSetChanged()
        }
    }

    private fun addItem()=startActivity(Intent(this, EditActivity::class.java))


    private fun showCommands() {
        val commands = adapter.getSelectCommands()
        if (commands.isNotEmpty()) MaterialAlertDialogBuilder(this).setTitle(R.string.command).setMessage(commands).show()
    }


    override fun onRestart() {
        super.onRestart()
        refresh()
    }

}

