package mperezf.mimo.gruposcaminosantiago.presentation.ui.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.group_detail_fragment.*

import mperezf.mimo.gruposcaminosantiago.R
import mperezf.mimo.gruposcaminosantiago.domain.model.Group
import mperezf.mimo.gruposcaminosantiago.presentation.extension.fromBase64
import mperezf.mimo.gruposcaminosantiago.presentation.extension.fromTimestamp


class GroupDetailFragment : Fragment(), OnMapReadyCallback {


    private var group: Group? = null
    private lateinit var googleMap: GoogleMap

    companion object {

        private const val GROUP_DETAIL = "group_detail"

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
            it.remove(GROUP_DETAIL)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.group_detail_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showDetailGroup()


        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
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

            departurePlace?.let {
                tv_group_departure_place.text = it
            }

            founder?.photo?.let {
                iv_avatar_group_admin.fromBase64(it)
            }

            founder?.name.let {
                tv_group_admin_name.text = it
            }

            founder?.email.let {
                tv_group_admin_email.text = it
            }
        }

    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        group?.let {
            val marker = LatLng(it.latitude!!, it.longitude!!)
            googleMap.addMarker(MarkerOptions().position(marker))
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(marker))
        }

        val googleMapOptions = GoogleMapOptions().liteMode(true)
        googleMap.mapType = googleMapOptions.mapType
        googleMap.uiSettings.setAllGesturesEnabled(false)
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(9.0f))

    }


}
