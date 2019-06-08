package mperezf.mimo.gruposcaminosantiago.presentation.ui.fragment

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.group_member_list_fragment.*

import mperezf.mimo.gruposcaminosantiago.R
import mperezf.mimo.gruposcaminosantiago.domain.model.Group
import mperezf.mimo.gruposcaminosantiago.domain.model.User
import mperezf.mimo.gruposcaminosantiago.presentation.ui.adapter.GroupMembersAdapter
import mperezf.mimo.gruposcaminosantiago.presentation.viewModel.GroupMemberListViewModel

class GroupMemberListFragment : BaseFragment(), View.OnClickListener {

    private var memberFragmentListener: MemberFragmentListener? = null
    private var group: Group? = null
    private var userId: Int? = 0
    private var isMember: Boolean = false

    companion object {

        private const val GROUP_DETAIL = "group_detail"

        @JvmStatic
        fun newInstance(group: Group) =
            GroupMemberListFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(GROUP_DETAIL, group)
                }
            }
    }


    private lateinit var viewModel: GroupMemberListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            group = it.getParcelable(GROUP_DETAIL)
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.group_member_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_group_members.adapter = GroupMembersAdapter()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(GroupMemberListViewModel::class.java)

        viewModel.getAuthenticatedUser { user ->
            userId = user.id
            group?.members?.let {
                showGroupMemberList(group)
                showMemberButton(user, group)
            }
        }

        addObservers()

    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        memberFragmentListener = activity as MemberFragmentListener
    }

    override fun onDetach() {
        memberFragmentListener = null
        super.onDetach()
    }

    override fun onDestroy() {
        viewModel.dispose()
        super.onDestroy()
    }


    private fun addObservers() {
        viewModel.getLoadingState().observe(this, Observer<Boolean> {
            if (it) {
                bt_switch_member.startAnimation()

            } else {
                bt_switch_member.revertAnimation()
            }
        })

        viewModel.getErrorMsg().observe(this, Observer<String> {
            showMessage(it)
        })

        viewModel.getMemberWasAdded().observe(this, Observer<Group> {
            isMember = true
            updateGroup(it)
            bt_switch_member.text = getString(R.string.leave_group)
            bt_switch_member.saveInitialState()
        })

        viewModel.getMemberWasRemoved().observe(this, Observer<Group> {
            isMember = false
            updateGroup(it)
            bt_switch_member.text = getString(R.string.add_member_to_group)
            bt_switch_member.saveInitialState()
        })
    }

    private fun updateGroup(it: Group) {
        group = it
        showGroupMemberList(it)
        memberFragmentListener?.updateGroup(it)
    }

    private fun showGroupMemberList(group: Group?) {
        group?.members?.let {
            (rv_group_members.adapter as GroupMembersAdapter).updateItems(ArrayList(it))
            rv_group_members.adapter?.notifyDataSetChanged()
        }

    }

    private fun showMemberButton(authenticatedUser: User, group: Group?) {
        if (authenticatedUser.id == group?.founder?.id) {
            bt_switch_member.visibility = View.GONE
        } else {
            bt_switch_member.visibility = View.VISIBLE
            bt_switch_member.setOnClickListener(this)
        }

        if (viewModel.checkIsMember(userId!!, group?.members!!)) {
            isMember = true
            bt_switch_member.text = getString(R.string.leave_group)
        } else {
            isMember = false
            bt_switch_member.text = getString(R.string.add_member_to_group)
        }

    }

    override fun onClick(view: View) {
        if (isMember) {
            viewModel.removeMemberGroup(group!!.id!!)
        } else {
            viewModel.addMemberToGroup(group!!.id!!)
        }
    }


    interface MemberFragmentListener {
        fun updateGroup(group: Group)
    }
}
