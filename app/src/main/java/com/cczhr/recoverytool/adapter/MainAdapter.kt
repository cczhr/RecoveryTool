package com.cczhr.recoverytool.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.cczhr.recoverytool.EditActivity
import com.cczhr.recoverytool.R
import com.cczhr.recoverytool.database.Command
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.item_main.view.*

/**
 * @author cczhr
 * @since  2020/9/6
 * @description https://github.com/cczhr
 */
class MainAdapter(var data: MutableList<Command>, private var context: Context) :
    RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.item_main, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
        holder.cardView.setOnClickListener {
            data[position].isSelect = !data[position].isSelect
            notifyItemChanged(position)
        }
        holder.edit.setOnClickListener {
            context.startActivity(
                Intent(context, EditActivity::class.java).putExtra(
                    "data",
                    data[position]
                )
            )

        }
    }


    fun getSelectCommands(): String {
        val stringBuilder = StringBuilder("")
        for(command in data){
            if(command.isSelect){
                stringBuilder.append("${command.commands}\n")
            }
        }
        return stringBuilder.toString()
    }


    fun getSelectItem(): List<Command> {
        val list=ArrayList<Command>()
        for(command in data){
            if(command.isSelect){
                list.add(command)
            }
        }
        return list
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title = itemView.title
        var commands = itemView.commands
        var cardView = itemView.card_view
        var edit = itemView.edit
        fun bind(command: Command) {
            title.text = command.title
            commands.text = command.commands
            cardView.isChecked = command.isSelect
        }
    }


    open class CardItemTouchHelperCallback(private val cardAdapter: MainAdapter) :
        ItemTouchHelper.Callback() {
        private var dragCardView: MaterialCardView? = null
        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int {
            return makeMovementFlags(DRAG_FLAGS, SWIPE_FLAGS)
        }

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            val fromPosition = viewHolder.adapterPosition
            val toPosition = target.adapterPosition
            swapCards(fromPosition, toPosition, cardAdapter)
            return true
        }

        override fun onSwiped(
            viewHolder: RecyclerView.ViewHolder,
            direction: Int
        ) {
        }

        override fun onSelectedChanged(
            viewHolder: RecyclerView.ViewHolder?,
            actionState: Int
        ) {
            super.onSelectedChanged(viewHolder, actionState)
            if (actionState == ItemTouchHelper.ACTION_STATE_DRAG && viewHolder != null) {
                dragCardView = viewHolder.itemView as MaterialCardView
                dragCardView!!.isDragged = true
            } else if (actionState == ItemTouchHelper.ACTION_STATE_IDLE && dragCardView != null) {
                dragCardView!!.isDragged = false
                dragCardView = null
            }
        }

        private fun swapCards(
            fromPosition: Int,
            toPosition: Int,
            cardAdapter: MainAdapter
        ) {
            val fromData = cardAdapter.data[fromPosition]
            cardAdapter.data[fromPosition] = cardAdapter.data[toPosition]
            cardAdapter.data[toPosition] = fromData
            cardAdapter.notifyItemMoved(fromPosition, toPosition)
            cardAdapter.notifyItemChanged(fromPosition)
            cardAdapter.notifyItemChanged(toPosition)
        }

        companion object {
            private const val DRAG_FLAGS = ItemTouchHelper.UP or ItemTouchHelper.DOWN
            private const val SWIPE_FLAGS = 0
        }

    }


}