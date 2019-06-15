package mperezf.mimo.gruposcaminosantiago.presentation.ui.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.group_list_fragment.*
import mperezf.mimo.gruposcaminosantiago.R
import mperezf.mimo.gruposcaminosantiago.domain.model.Group
import mperezf.mimo.gruposcaminosantiago.presentation.ui.activity.AddGroupActivity
import mperezf.mimo.gruposcaminosantiago.presentation.ui.activity.GroupDetailActivity
import mperezf.mimo.gruposcaminosantiago.presentation.ui.adapter.GroupsAdapter
import mperezf.mimo.gruposcaminosantiago.presentation.viewModel.GroupListViewModel

class GroupListFragment : BaseFragment() {

    companion object {

        const val RELOAD_LIST = "reload_groups_list"
        const val GROUP_DETAIL = 1
        const val ADD_GROUP = 2

        @JvmStatic
        fun newInstance(): GroupListFragment = GroupListFragment()
    }

    private lateinit var viewModel: GroupListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.group_list_fragment, container, false)
        setHasOptionsMenu(true)

        return view
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(GroupListViewModel::class.java)
        createGroupsAdapter()
        addObservers()
        viewModel.getGroups()

    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_add_group -> {
                showAddGroupFragment()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK)
            when (requestCode) {
                GROUP_DETAIL, ADD_GROUP -> {
                    data?.getBooleanExtra(RELOAD_LIST, false)?.let { reload ->
                        if (reload) {
                            viewModel.getGroups()
                        }
                    }
                }
            }
    }

    private fun showAddGroupFragment() {
        startActivityForResult(Intent(activity, AddGroupActivity::class.java), ADD_GROUP)
        activity?.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        // AddGroupDialogFragment.newInstance().show(childFragmentManager, AddGroupDialogFragment.TAG)
    }

    private fun createGroupsAdapter() {
        rv_groups.adapter = GroupsAdapter {
            groupSelected(it)
        }
    }

    private fun groupSelected(selectedGroup: Group) {
        val intent = Intent(context, GroupDetailActivity::class.java)
        intent.putExtra(GroupDetailActivity.GROUP_ID, selectedGroup.id)
        intent.putExtra(GroupDetailActivity.GROUP_TITLE, selectedGroup.title)
        startActivityForResult(intent, GROUP_DETAIL)
        activity?.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    private fun addObservers() {
        viewModel.getErrorMsg().observe(this, Observer<String> {
            showMessage(it)
        })

        viewModel.getLoadingState().observe(this, Observer<Boolean> {
            if (it) {
                pb_groups.visibility = View.VISIBLE
            } else {
                pb_groups.visibility = View.GONE
                srl_group_list.isRefreshing = false
            }
        })

        viewModel.getGroupsUpdate().observe(this, Observer<List<Group>> {
            showGroupList(it)
        })

        srl_group_list.setOnRefreshListener {
            if (pb_groups.visibility != View.VISIBLE) {
                viewModel.getGroups()
            }
        }
    }

    private fun showGroupList(groupList: List<Group>?) {
        if (groupList != null && groupList.isNotEmpty()) {
            no_items_view.visibility = View.GONE
            (rv_groups.adapter as GroupsAdapter).updateItems(groupList)

        } else {
            no_items_view.visibility = View.VISIBLE
            (rv_groups.adapter as GroupsAdapter).updateItems(emptyList())
        }

        rv_groups.adapter?.notifyDataSetChanged()
    }

}
