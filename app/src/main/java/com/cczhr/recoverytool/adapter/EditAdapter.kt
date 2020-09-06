package com.cczhr.recoverytool.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.cczhr.recoverytool.R
import com.cczhr.recoverytool.utils.COMMOM_COMMAND
import com.cczhr.recoverytool.utils.TWRP_COMMAND
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.item_edit.view.*

/**
 * @author cczhr
 * @since  2020/8/29 10:41
 * @description https://github.com/cczhr
 */
class EditAdapter(var editText: EditText,var onClickListener: (type: Int, position: Int, text: String) -> Unit) : RecyclerView.Adapter<EditAdapter.ViewHolder>() {
    private var data = ArrayList<String>()
    private var type = 0


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.item_edit, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }


    fun commonCommand() {
        editText.setText("")
        type = 1
        val size = data.size
        data.clear()
        notifyItemRangeRemoved(0, size)
        data.addAll(COMMOM_COMMAND)
        notifyItemRangeInserted(0, data.size)
    }

    fun twrpCommand() {
        editText.setText("")
        type = 2
        val size = data.size
        data.clear()
        notifyItemRangeRemoved(0, size)
        data.addAll(TWRP_COMMAND)
        notifyItemRangeInserted(0, data.size)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
        holder.commandView.setOnClickListener {
            onClickListener(type, position, holder.commandView.text.toString())

        }

    }



class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var commandView: Chip = itemView.command
    fun bind(command: String) {
        commandView.text = command
    }
}





}