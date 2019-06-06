package mperezf.mimo.gruposcaminosantiago.presentation.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.group_list_fragment.*
import mperezf.mimo.gruposcaminosantiago.R
import mperezf.mimo.gruposcaminosantiago.domain.model.Group
import mperezf.mimo.gruposcaminosantiago.presentation.ui.activity.GroupDetailActivity
import mperezf.mimo.gruposcaminosantiago.presentation.ui.adapter.GroupsAdapter
import mperezf.mimo.gruposcaminosantiago.presentation.viewModel.GroupListViewModel

class GroupListFragment : BaseFragment() {

    companion object {
        private const val USER_ID = "user_id"

        @JvmStatic
        fun newInstance(userId: Int): GroupListFragment =
            GroupListFragment().apply {
                arguments = Bundle().apply {
                    putInt(USER_ID, userId)
                }
            }
    }

    private lateinit var viewModel: GroupListViewModel
    private var userId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userId = it.getInt(USER_ID)
        }
    }

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
        viewModel.getGroups(userId)

    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_add_group -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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
        startActivity(intent)
        activity?.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    private fun addObservers() {
        viewModel.getErrorMsg().observe(this, Observer<String> {
            showMessage(it)
        })

        viewModel.getLoadingState().observe(this, Observer<Boolean> {
            if (it) {
                if (!srl_group_list.isRefreshing) {
                    pb_groups.visibility = View.VISIBLE
                }
            } else {
                pb_groups.visibility = View.GONE
                srl_group_list.isRefreshing = false
            }
        })

        viewModel.getGroupsUpdate().observe(this, Observer<List<Group>> {
            showGroupList(it)
        })

        srl_group_list.setOnRefreshListener {
            viewModel.getGroups(userId)
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