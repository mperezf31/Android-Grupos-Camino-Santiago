package mperezf.mimo.gruposcaminosantiago.presentation.ui.fragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.group_member_list_fragment.*

import mperezf.mimo.gruposcaminosantiago.R
import mperezf.mimo.gruposcaminosantiago.domain.model.Group
import mperezf.mimo.gruposcaminosantiago.domain.model.User
import mperezf.mimo.gruposcaminosantiago.presentation.ui.adapter.GroupMembersAdapter
import mperezf.mimo.gruposcaminosantiago.presentation.viewModel.GroupMemberListViewModel

class GroupMemberListFragment : Fragment() {


    private var members: ArrayList<User>? = ArrayList()

    companion object {

        private const val GROUP_MEMBERS = "group_members"

        @JvmStatic
        fun newInstance(members: ArrayList<User>?) =
            GroupMemberListFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(GROUP_MEMBERS, members)
                }
            }
    }


    private lateinit var viewModel: GroupMemberListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            members = it.getParcelableArrayList(GROUP_MEMBERS)
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.group_member_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_group_members.adapter = GroupMembersAdapter()
        showGroupMemberList()
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(GroupMemberListViewModel::class.java)

    }


    private fun showGroupMemberList() {
        members?.let {
            (rv_group_members.adapter as GroupMembersAdapter).updateItems(it)
            rv_group_members.adapter?.notifyDataSetChanged()
        }

    }

}
