package mperezf.mimo.gruposcaminosantiago.presentation.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.group_member_list_fragment.*
import mperezf.mimo.gruposcaminosantiago.CaminoDeSantiagoApp
import mperezf.mimo.gruposcaminosantiago.R
import mperezf.mimo.gruposcaminosantiago.domain.model.Group
import mperezf.mimo.gruposcaminosantiago.domain.model.User
import mperezf.mimo.gruposcaminosantiago.presentation.ui.activity.UpdateGroupDetailListener
import mperezf.mimo.gruposcaminosantiago.presentation.ui.adapter.GroupMembersAdapter
import mperezf.mimo.gruposcaminosantiago.presentation.viewModel.GroupMemberListViewModel

class GroupMemberListFragment : BaseFragment(), View.OnClickListener {

    private var fragmentListener: UpdateGroupDetailListener? = null
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
            it.remove(GROUP_DETAIL)
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

        val factory = GroupMemberListViewModel.Factory(application = activity?.application as CaminoDeSantiagoApp)
        viewModel = ViewModelProviders.of(this, factory).get(GroupMemberListViewModel::class.java)

        viewModel.getAuthenticatedUser({ user ->
            userId = user.id
            group?.members?.let {
                showGroupMemberList(group)
                showMemberButtons(user, group)
            }
        }, {})

        addObservers()

    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        fragmentListener = activity as UpdateGroupDetailListener
    }

    override fun onDetach() {
        fragmentListener = null
        super.onDetach()
    }

    override fun onDestroy() {
        viewModel.dispose()
        super.onDestroy()
    }


    private fun addObservers() {
        viewModel.getLoadingState().observe(this, Observer<Boolean> {
            if (it) {
                if (isMember) {
                    bt_remove_member.startAnimation()
                } else {
                    bt_add_member.startAnimation()
                }
            } else {
                bt_add_member.revertAnimation()
                bt_remove_member.revertAnimation()
            }
        })

        viewModel.getErrorMsg().observe(this, Observer<String> {
            showMessage(it)
        })

        viewModel.getMemberWasAdded().observe(this, Observer<Group> {
            updateGroup(it)
            prepareButtons(true)

        })

        viewModel.getMemberWasRemoved().observe(this, Observer<Group> {
            updateGroup(it)
            prepareButtons(false)
        })
    }

    private fun updateGroup(it: Group) {
        group = it
        showGroupMemberList(it)
        fragmentListener?.updateGroup(it, true)
    }

    private fun showGroupMemberList(group: Group?) {
        group?.members?.let {
            (rv_group_members.adapter as GroupMembersAdapter).updateItems(viewModel.getGroupMembers(group))
            rv_group_members.adapter?.notifyDataSetChanged()
        }

    }

    private fun showMemberButtons(authenticatedUser: User, group: Group?) {
        if (authenticatedUser.id != group?.founder?.id) {
            bt_add_member.setOnClickListener(this)
            bt_remove_member.setOnClickListener(this)

            prepareButtons(viewModel.checkIsMember(userId!!, group?.members!!))
        }
    }

    private fun prepareButtons(isMember: Boolean) {
        if (isMember) {
            this.isMember = true
            bt_add_member.visibility = View.GONE
            bt_remove_member.visibility = View.VISIBLE
        } else {
            this.isMember = false
            bt_add_member.visibility = View.VISIBLE
            bt_remove_member.visibility = View.GONE
        }
    }

    override fun onClick(view: View) {
        if (view.id == bt_add_member.id) {
            viewModel.addMemberToGroup(group!!.id!!)
        } else if (view.id == bt_remove_member.id) {
            viewModel.removeMemberGroup(group!!.id!!)
        }
    }

}
