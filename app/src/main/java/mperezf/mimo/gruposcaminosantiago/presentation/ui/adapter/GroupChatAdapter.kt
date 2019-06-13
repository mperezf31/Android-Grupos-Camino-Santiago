package mperezf.mimo.gruposcaminosantiago.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_chat_1.view.*
import mperezf.mimo.gruposcaminosantiago.R
import mperezf.mimo.gruposcaminosantiago.domain.model.Message
import mperezf.mimo.gruposcaminosantiago.presentation.extension.fromTimestamp

class GroupChatAdapter() : RecyclerView.Adapter<GroupChatAdapter.ViewHolder>() {

    private var items: ArrayList<Message> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_chat_1, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val member = items[position]
        viewHolder.bind(member)
    }

    fun updateItems(newItems: ArrayList<Message>) {
        this.items = newItems
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvmsg: TextView = itemView.tv_msg
        var tvDate: TextView = itemView.tv_msg_date

        fun bind(message: Message) {

            message.apply {
                tvmsg.text = content?.trim() ?: ""
                whenSent?.let { tvDate.fromTimestamp(it) }
            }
        }
    }
}