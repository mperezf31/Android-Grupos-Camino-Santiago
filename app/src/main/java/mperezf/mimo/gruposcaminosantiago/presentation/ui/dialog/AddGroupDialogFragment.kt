package mperezf.mimo.gruposcaminosantiago.presentation.ui.dialog

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment
import kotlinx.android.synthetic.main.add_group_fragment.*
import mperezf.mimo.gruposcaminosantiago.R
import mperezf.mimo.gruposcaminosantiago.presentation.extension.fromTimestamp
import mperezf.mimo.gruposcaminosantiago.presentation.ui.activity.MapsActivity
import mperezf.mimo.gruposcaminosantiago.presentation.viewModel.AddGroupViewModel
import java.util.*


class AddGroupDialogFragment : DialogFragment() {


    companion object {
        const val DEPARTURE_DIALOG_TAG: String = "departure_dialog"
        const val ARRIVAL_DIALOG_TAG: String = "arrival_dialog"

        private const val PERMISSION_REQUEST_CODE = 1
        private const val GALLERY_REQUEST_CODE = 2
        private const val REQUEST_LOCATION: Int = 3
        private const val DATE_PATTERN = "hh:mm dd/mm/yy"

        val TAG: String = AddGroupDialogFragment::class.java.simpleName
        fun newInstance() = AddGroupDialogFragment()
    }


    private lateinit var viewModel: AddGroupViewModel
    private lateinit var departureDialogFragment: SwitchDateTimeDialogFragment
    private lateinit var arrivalDialogFragment: SwitchDateTimeDialogFragment
    private lateinit var etDepartureDate: TextView
    private lateinit var etArrivalDate: TextView

    private var departureDate: Date = Date()
    private var arrivalDate: Date = Date()
    private var groupLat: Int? = 0
    private var groupLng: Int? = 0

    private var bitmapGroupImage: Bitmap? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.add_group_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    /*    toolbar.setNavigationIcon(R.drawable.ic_close)
        toolbar.title = getString(R.string.create_group)

        toolbar.setNavigationOnClickListener {
            dismiss()
        }
*/
        etDepartureDate = view.findViewById(R.id.et_departure_date_value)
        etArrivalDate = view.findViewById(R.id.et_arrival_date_value)

        addListeners()

        initDateTimeDialogs()

    }

    override fun onStart() {
        super.onStart()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            goToGallery()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK)
            when (requestCode) {
                GALLERY_REQUEST_CODE -> {
                    val selectedImage: Uri? = data?.data
                    bitmapGroupImage = MediaStore.Images.Media.getBitmap(activity?.contentResolver, selectedImage)
                    iv_group_image.setImageBitmap(bitmapGroupImage)
                }
                REQUEST_LOCATION -> {
                    groupLat = data?.getIntExtra(MapsActivity.MAP_LAT, 0)
                    groupLng = data?.getIntExtra(MapsActivity.MAP_LNG, 0)
                }
            }
    }

    private fun addListeners() {

        bt_select_group_image.setOnClickListener {
            if (Build.VERSION.SDK_INT >= 23) {
                if (activity?.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    goToGallery()
                } else {
                    requestPermissions(
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        PERMISSION_REQUEST_CODE
                    )
                }
            } else {
                goToGallery()
            }
        }

        etDepartureDate.setOnClickListener {
            departureDialogFragment.show(
                childFragmentManager,
                DEPARTURE_DIALOG_TAG
            )
        }

        etArrivalDate.setOnClickListener {
            arrivalDialogFragment.show(
                childFragmentManager,
                ARRIVAL_DIALOG_TAG
            )
        }

        et_departure_coordinates.setOnClickListener {
            startActivityForResult(Intent(context, MapsActivity::class.java), REQUEST_LOCATION)
        }
    }


    private fun initDateTimeDialogs() {
        departureDialogFragment = SwitchDateTimeDialogFragment.newInstance(
            getString(R.string.group_departure_date),
            getString(android.R.string.ok),
            getString(android.R.string.cancel)
        )

        with(departureDialogFragment) {
            setTimeZone(TimeZone.getDefault())
            startAtCalendarView()
            set24HoursMode(true)
            minimumDateTime = departureDate
            setDefaultDateTime(departureDate)
            setOnButtonClickListener(object :
                SwitchDateTimeDialogFragment.OnButtonClickListener {

                override fun onNegativeButtonClick(date: Date?) {}

                override fun onPositiveButtonClick(date: Date) {
                    departureDate = date
                    etDepartureDate.fromTimestamp(
                        date.time,
                        DATE_PATTERN
                    )
                }

            })
        }

        arrivalDialogFragment = SwitchDateTimeDialogFragment.newInstance(
            getString(R.string.group_arrival_Date),
            getString(android.R.string.ok),
            getString(android.R.string.cancel)
        )

        with(arrivalDialogFragment) {
            setTimeZone(TimeZone.getDefault())
            startAtCalendarView()
            set24HoursMode(true)
            minimumDateTime = arrivalDate
            setDefaultDateTime(arrivalDate)

            setOnButtonClickListener(object :
                SwitchDateTimeDialogFragment.OnButtonClickListener {

                override fun onNegativeButtonClick(date: Date?) {}

                override fun onPositiveButtonClick(date: Date) {
                    arrivalDate = date
                    etArrivalDate.fromTimestamp(
                        date.time,
                        DATE_PATTERN
                    )
                }

            })
        }
    }

    private fun goToGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        val mimeTypes = arrayOf("image/jpeg", "image/png")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        startActivityForResult(
            intent,
            GALLERY_REQUEST_CODE
        )
    }

}
