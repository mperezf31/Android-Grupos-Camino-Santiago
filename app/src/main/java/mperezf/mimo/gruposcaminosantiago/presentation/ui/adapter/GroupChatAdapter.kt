package mperezf.mimo.gruposcaminosantiago.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_chat_2.view.*
import mperezf.mimo.gruposcaminosantiago.R
import mperezf.mimo.gruposcaminosantiago.domain.model.Message
import mperezf.mimo.gruposcaminosantiago.presentation.extension.fromBase64
import mperezf.mimo.gruposcaminosantiago.presentation.extension.fromTimestamp

class GroupChatAdapter(val userId: Int) : RecyclerView.Adapter<GroupChatAdapter.BaseViewHolder>() {

    companion object {
        private const val ITEM_USER_AUTHENTICATED = 1
        private const val ITEM_OTHER_USERS = 2
    }

    private var items: ArrayList<Message> = ArrayList()

    override fun getItemViewType(position: Int): Int {
        return if (items[position].author?.id == userId) {
            ITEM_USER_AUTHENTICATED
        } else {
            ITEM_OTHER_USERS
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return if (viewType == ITEM_USER_AUTHENTICATED) {
            ViewHolderAuthenticatedUser(
                LayoutInflater.from(parent.context).inflate(R.layout.item_chat_1, parent, false)
            )

        } else {
            ViewHolderOtherUsers(LayoutInflater.from(parent.context).inflate(R.layout.item_chat_2, parent, false))
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(viewHolder: BaseViewHolder, position: Int) {
        val member = items[position]
        viewHolder.bind(member)
    }

    fun updateItems(newItems: ArrayList<Message>) {
        this.items = newItems
    }

    class ViewHolderAuthenticatedUser(itemView: View) : BaseViewHolder(itemView)

    class ViewHolderOtherUsers(itemView: View) : BaseViewHolder(itemView) {
        var ivAvatar: ImageView = itemView.iv_msg

        override fun bind(message: Message) {
            super.bind(message)
            message.apply {
                author?.photo?.let { ivAvatar.fromBase64(it) }
            }
        }
    }

    open class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvMsg: TextView = itemView.findViewById(R.id.tv_msg)
        var tvDate: TextView = itemView.findViewById(R.id.tv_msg_date)

        open fun bind(message: Message) {
            message.apply {
                tvMsg.text = content?.trim() ?: ""
                whenSent?.let { tvDate.fromTimestamp(it, "HH:mm dd/MM/yy") }
            }
        }
    }
}