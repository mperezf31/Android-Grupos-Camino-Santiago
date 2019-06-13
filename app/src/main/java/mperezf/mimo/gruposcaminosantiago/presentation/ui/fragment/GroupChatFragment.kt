package mperezf.mimo.gruposcaminosantiago.presentation.ui.fragment

import android.content.Context
import android.os.Bundle
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.group_member_list_fragment.*
import mperezf.mimo.gruposcaminosantiago.R
import mperezf.mimo.gruposcaminosantiago.domain.model.Group
import mperezf.mimo.gruposcaminosantiago.presentation.ui.adapter.GroupMembersAdapter
import mperezf.mimo.gruposcaminosantiago.presentation.viewModel.GroupChatViewModel

class GroupChatFragment : BaseFragment() {

    private var chatFragmentListener: ChatFragmentListener? = null
    private var group: Group? = null

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
        rv_group_members.adapter = GroupMembersAdapter()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(GroupChatViewModel::class.java)


        showGroupMemberList(group)

        addObservers()

    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        chatFragmentListener = activity as ChatFragmentListener
    }

    override fun onDetach() {
        chatFragmentListener = null
        super.onDetach()
    }

    override fun onDestroy() {
        viewModel.dispose()
        super.onDestroy()
    }


    private fun addObservers() {
        viewModel.getLoadingState().observe(this, Observer<Boolean> {
            if (it) {

            } else {

            }
        })

        viewModel.getErrorMsg().observe(this, Observer<String> {
            showMessage(it)
        })

        viewModel.getSendMessage().observe(this, Observer<Group> {
            updateGroup(it)
          //  updateMessages(null)

        })
    }


    private fun updateMessages(messages: List<Message>) {


    }

    private fun updateGroup(it: Group) {
        group = it
        showGroupMemberList(it)
        chatFragmentListener?.updateGroup(it)
    }

    private fun showGroupMemberList(group: Group?) {

    }

    interface ChatFragmentListener {
        fun updateGroup(group: Group)
    }
}
