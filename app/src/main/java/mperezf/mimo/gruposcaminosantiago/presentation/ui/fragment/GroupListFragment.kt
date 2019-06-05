package mperezf.mimo.gruposcaminosantiago.presentation.ui.fragment

import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.group_list_fragment.*
import mperezf.mimo.gruposcaminosantiago.R
import mperezf.mimo.gruposcaminosantiago.domain.model.Group
import mperezf.mimo.gruposcaminosantiago.presentation.ui.adapter.GroupsAdapter
import mperezf.mimo.gruposcaminosantiago.presentation.ui.viewModel.GroupListViewModel

class GroupListFragment : BaseFragment() {

    companion object {
        private const val USER_ID = "user_id"

        fun newInstance(userId: Int): GroupListFragment {
            val args = Bundle()
            args.putSerializable(USER_ID, userId)
            val fragment = GroupListFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var viewModel: GroupListViewModel
    private var userId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getInt(USER_ID)?.let {
            userId = it
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
            R.id.order_by_distance -> {
                item.isChecked = true
                true
            }
            R.id.order_by_departure_date -> {
                item.isChecked = true
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
