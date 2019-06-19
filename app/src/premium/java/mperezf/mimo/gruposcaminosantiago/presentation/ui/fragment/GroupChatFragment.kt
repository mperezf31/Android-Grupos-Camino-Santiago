package mperezf.mimo.gruposcaminosantiago.presentation.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.premium.group_chat_fragment.*
import mperezf.mimo.gruposcaminosantiago.CaminoDeSantiagoApp
import mperezf.mimo.gruposcaminosantiago.R
import mperezf.mimo.gruposcaminosantiago.domain.model.Group
import mperezf.mimo.gruposcaminosantiago.presentation.ui.activity.UpdateGroupDetailListener
import mperezf.mimo.gruposcaminosantiago.presentation.ui.adapter.GroupChatAdapter
import mperezf.mimo.gruposcaminosantiago.presentation.viewModel.GroupChatViewModel


class GroupChatFragment : BaseFragment() {

    private var fragmentListener: UpdateGroupDetailListener? = null
    private var group: Group? = null

    private var userId: Int = 0

    companion object {

        private const val GROUP_DETAIL = "group_detail"

        @JvmStatic
        fun newInstance(group: Group) =
            GroupChatFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(GROUP_DETAIL, group)
                }
            }
    }


    private lateinit var viewModel: GroupChatViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            group = it.getParcelable(GROUP_DETAIL)
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.group_chat_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        pb_chat.hide()

        val layoutManager = LinearLayoutManager(activity)
        layoutManager.reverseLayout = true
        rv_group_chat.layoutManager = layoutManager

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val factory = GroupChatViewModel.Factory(application = activity?.application as CaminoDeSantiagoApp)
        viewModel = ViewModelProviders.of(this, factory).get(GroupChatViewModel::class.java)

        viewModel.getAuthenticatedUser({ user ->
            user.id?.let { id ->
                userId = id
            }

            rv_group_chat.adapter = GroupChatAdapter(userId)

            addObservers()
            addListeners()

            group?.let {
                updateMessages(it)
            }
        }, {})
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.chat, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_refresh -> {
                if (!pb_chat.isShown){
                    group?.id?.let {
                        viewModel.getGroupDetail(it)
                    }
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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

        viewModel.getRefreshState().observe(this, Observer<Boolean> {
            if (it) {
                pb_chat.show()
            } else {
                pb_chat.hide()
            }
        })

        viewModel.getSendingState().observe(this, Observer<Boolean> {
            if (it) {
                bt_add_message.startAnimation()
            } else {
                bt_add_message.revertAnimation()
            }
        })

        viewModel.getErrorMsg().observe(this, Observer<String> {
            showMessage(it)
        })

        viewModel.getUpdateMessage().observe(this, Observer<Group> {
            et_msg.setText("")
            updateGroup(it)
        })
    }

    private fun addListeners() {
        bt_add_message.setOnClickListener {
            if (et_msg.text.isNotEmpty()) {
                group?.id?.let { id ->
                    viewModel.sendMessage(id, et_msg.text.toString())
                }
            }
        }
    }

    private fun updateGroup(it: Group) {
        group = it
        updateMessages(it)
        fragmentListener?.updateGroup(it, false)
    }

    private fun updateMessages(group: Group) {
        group.messages?.let {
            (rv_group_chat.adapter as GroupChatAdapter).updateItems(ArrayList(it.reversed()))
            rv_group_chat.adapter?.notifyDataSetChanged()

        }

    }
}
