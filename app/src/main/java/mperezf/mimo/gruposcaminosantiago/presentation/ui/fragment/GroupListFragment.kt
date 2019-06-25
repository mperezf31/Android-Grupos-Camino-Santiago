package mperezf.mimo.gruposcaminosantiago.presentation.ui.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.group_list_fragment.*
import mperezf.mimo.gruposcaminosantiago.CaminoDeSantiagoApp
import mperezf.mimo.gruposcaminosantiago.R
import mperezf.mimo.gruposcaminosantiago.domain.model.Group
import mperezf.mimo.gruposcaminosantiago.presentation.ui.activity.AddGroupActivity
import mperezf.mimo.gruposcaminosantiago.presentation.ui.activity.GroupDetailActivity
import mperezf.mimo.gruposcaminosantiago.presentation.ui.adapter.GroupsAdapter
import mperezf.mimo.gruposcaminosantiago.presentation.viewModel.GroupListViewModel

class GroupListFragment : BaseFragment() {

    companion object {

        private const val PERMISSION_REQUEST_CODE = 1

        const val RELOAD_LIST = "reload_groups_list"
        const val GROUP_DETAIL = 1
        const val ADD_GROUP = 2

        @JvmStatic
        fun newInstance(): GroupListFragment = GroupListFragment()
    }

    private var userLocation: Location? = null
    private lateinit var viewModel: GroupListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.group_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val factory = GroupListViewModel.Factory(application = activity?.application as CaminoDeSantiagoApp)
        viewModel = ViewModelProviders.of(this, factory).get(GroupListViewModel::class.java)

        if (Build.VERSION.SDK_INT >= 23) {
            if (activity?.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                initGroupList(true)
            } else {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_CODE)
            }
        } else {
            initGroupList(true)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initGroupList(true)
        } else {
            initGroupList(false)
        }
    }

    @SuppressLint("MissingPermission")
    private fun initGroupList(permissionsGranted: Boolean) {
        createGroupsAdapter()
        addObservers()
        addListeners()

        if (permissionsGranted) {
            val locationClient = LocationServices.getFusedLocationProviderClient(activity as AppCompatActivity)
            locationClient.lastLocation.addOnSuccessListener { location: Location? ->
                userLocation = location
                viewModel.getGroups(userLocation)
            }
        } else {
            viewModel.getGroups()
        }


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
                            viewModel.getGroups(userLocation)
                        }
                    }
                }
            }
    }

    private fun showAddGroupFragment() {
        startActivityForResult(Intent(activity, AddGroupActivity::class.java), ADD_GROUP)
        activity?.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
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


    }

    private fun addListeners() {
        srl_group_list.setOnRefreshListener {
            if (!pb_groups.isShown) {
                viewModel.getGroups(userLocation)
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
