package mperezf.mimo.gruposcaminosantiago.presentation.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.map_fragment.*
import mperezf.mimo.gruposcaminosantiago.R


class MapDialogFragment : DialogFragment(), OnMapReadyCallback, GoogleMap.OnMapClickListener {


    companion object {
        val TAG: String = MapDialogFragment::class.java.simpleName
        fun newInstance() = MapDialogFragment()
    }

    private var googleMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.map_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*    toolbar.setNavigationIcon(R.drawable.ic_close)
            toolbar.title = getString(R.string.departure_zone)

            toolbar.setNavigationOnClickListener {
                dismiss()
            }
    */
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapFullScreen) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onStart() {
        super.onStart()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }


    override fun onMapReady(map: GoogleMap) {
        googleMap = map
    }

    override fun onMapClick(it: LatLng) {
        googleMap?.clear()
        googleMap?.addMarker(MarkerOptions().position(it))
    }


}
