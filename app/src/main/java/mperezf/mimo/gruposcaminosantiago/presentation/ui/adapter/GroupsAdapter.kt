package mperezf.mimo.gruposcaminosantiago.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_group.view.*
import mperezf.mimo.gruposcaminosantiago.R
import mperezf.mimo.gruposcaminosantiago.domain.model.Group
import mperezf.mimo.gruposcaminosantiago.presentation.extension.fromBase64
import mperezf.mimo.gruposcaminosantiago.presentation.extension.fromTimestamp

class GroupsAdapter(val listener: (Group) -> Unit) :
    RecyclerView.Adapter<GroupsAdapter.ViewHolder>() {

    private var items: List<Group> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_group, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val group = items[position]
        viewHolder.bind(group)

        viewHolder.itemView.setOnClickListener {
            listener(group)
        }
    }

    fun updateItems(newItems: List<Group>) {
        this.items = newItems
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivGroup: ImageView = itemView.iv_group
        var tvGroupName: TextView = itemView.tv_group_name
        var tvDeparturePlace: TextView = itemView.tv_group_departure_place
        var tvDepartureDate: TextView = itemView.tv_group_date_departure

        fun bind(group: Group) {
            group.photo?.let { ivGroup.fromBase64(it) }
            tvGroupName.text = group.title?.trim() ?: ""
            tvDeparturePlace.text.let { tvDeparturePlace.text = group.departurePlace?.trim() }
            group.departureDate?.let {
                tvDepartureDate.fromTimestamp(it)
            }

        }
    }
}