package mperezf.mimo.gruposcaminosantiago.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_group.view.*
import kotlinx.android.synthetic.main.item_member.view.*
import mperezf.mimo.gruposcaminosantiago.R
import mperezf.mimo.gruposcaminosantiago.domain.model.Group
import mperezf.mimo.gruposcaminosantiago.domain.model.User
import mperezf.mimo.gruposcaminosantiago.presentation.extension.fromBase64
import mperezf.mimo.gruposcaminosantiago.presentation.extension.fromTimestamp
import java.lang.reflect.Member

class GroupMembersAdapter() : RecyclerView.Adapter<GroupMembersAdapter.ViewHolder>() {

    private var items: ArrayList<User> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_member, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val member = items[position]
        viewHolder.bind(member)
    }

    fun updateItems(newItems: ArrayList<User>) {
        this.items = newItems
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivMember: ImageView = itemView.iv_member
        var tvName: TextView = itemView.tv_member_name
        var tvEmail: TextView = itemView.tv_member_email

        fun bind(member: User) {

            member.apply {
                photo?.let { ivMember.fromBase64(it) }
                tvName.text = name?.trim() ?: ""
                tvEmail.text = email?.trim() ?: ""

            }
        }
    }
}