package mperezf.mimo.gruposcaminosantiago.presentation.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_maps.*
import mperezf.mimo.gruposcaminosantiago.R


class MapsActivity : BaseActivity(), OnMapReadyCallback, GoogleMap.OnMapClickListener {


    companion object {
        const val MAP_LAT: String = "latitude"
        const val MAP_LNG: String = "longitude"

        private const val PERMISSION_REQUEST_CODE = 1
    }

    private lateinit var map: GoogleMap

    private var point: LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationIcon(R.drawable.ic_close)
        title = getString(R.string.add_location)

        val lat = intent.getDoubleExtra(MAP_LAT, 0.0)
        val lng = intent.getDoubleExtra(MAP_LNG, 0.0)

        if (lat != 0.0 && lng != 0.0) {
            point = LatLng(lat, lng)
        }

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_maps, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.menu_ok -> {
                if (point != null) {
                    returnLocation()
                } else {
                    showMessage(getString(R.string.click_to_map))
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun returnLocation() {
        if (point != null) {
            val output = Intent()
            output.putExtra(MAP_LAT, point!!.latitude)
            output.putExtra(MAP_LNG, point!!.longitude)
            setResult(Activity.RESULT_OK, output)
            finish()
        } else {
            onBackPressed()
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            showMyLocation()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.setOnMapClickListener(this)


        if (point != null) {
            moveMapToLocation(point!!, true)
        } else {
            //Move map to user location
            if (Build.VERSION.SDK_INT >= 23) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    showMyLocation()
                } else {
                    requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_CODE)
                }
            } else {
                showMyLocation()
            }
        }

    }

    private fun moveMapToLocation(point: LatLng, addMarker: Boolean = false) {
        map.moveCamera(CameraUpdateFactory.newLatLng(point))
        map.animateCamera(CameraUpdateFactory.zoomTo(9.0f))
        if (addMarker) {
            map.addMarker(MarkerOptions().position(point))
        }
    }

    @SuppressLint("MissingPermission")
    private fun showMyLocation() {
        map.isMyLocationEnabled = true

        val locationClient = LocationServices.getFusedLocationProviderClient(this)
        locationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                moveMapToLocation(LatLng(location.latitude, location.longitude))
            }
        }
    }

    override fun onMapClick(it: LatLng) {
        point = it
        map.clear()
        map.addMarker(MarkerOptions().position(it))
        map.moveCamera(CameraUpdateFactory.newLatLng(it))
    }


}
