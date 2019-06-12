package mperezf.mimo.gruposcaminosantiago.presentation.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_maps.*
import mperezf.mimo.gruposcaminosantiago.R


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMapClickListener {

    companion object {
        const val MAP_LAT: String = "latitude"
        const val MAP_LNG: String = "longitude"

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
                returnLocation()
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

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        val sydney = LatLng(-34.0, 151.0)
        map.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        map.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    override fun onMapClick(it: LatLng) {
        point = it
        map.clear()
        map.addMarker(MarkerOptions().position(it))
    }


}
