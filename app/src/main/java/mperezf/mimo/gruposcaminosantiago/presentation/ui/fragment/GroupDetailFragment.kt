package mperezf.mimo.gruposcaminosantiago.presentation.ui.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.group_detail_fragment.*

import mperezf.mimo.gruposcaminosantiago.R
import mperezf.mimo.gruposcaminosantiago.domain.model.Group
import mperezf.mimo.gruposcaminosantiago.presentation.extension.fromBase64
import mperezf.mimo.gruposcaminosantiago.presentation.extension.fromTimestamp


class GroupDetailFragment : Fragment() {

    private var group: Group? = null

    companion object {

        private const val GROUP_DETAIL = "group_detail"
        private const val ARG_PARAM2 = "param2"

        @JvmStatic
        fun newInstance(group: Group) =
            GroupDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(GROUP_DETAIL, group)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            group = it.getParcelable(GROUP_DETAIL)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.group_detail_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showDetailGroup()
    }

    private fun showDetailGroup() {

        group?.apply {
            photo?.let { iv_group.fromBase64(it) }
            departureDate?.let {
                tv_group_departure_date.fromTimestamp(it)
            }

            arrivalDate?.let {
                tv_group_arrival_date.fromTimestamp(it)
            }

            description?.let {
                tv_group_description.text = it
            }
        }


    }


}
