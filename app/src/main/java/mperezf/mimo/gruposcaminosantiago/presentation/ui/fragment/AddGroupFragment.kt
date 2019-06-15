package mperezf.mimo.gruposcaminosantiago.presentation.ui.fragment

import android.Manifest
import android.app.Activity
import android.content.Context
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
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment
import kotlinx.android.synthetic.main.add_group_fragment.*
import mperezf.mimo.gruposcaminosantiago.R
import mperezf.mimo.gruposcaminosantiago.domain.model.Group
import mperezf.mimo.gruposcaminosantiago.presentation.extension.*
import mperezf.mimo.gruposcaminosantiago.presentation.ui.activity.AddGroupListener
import mperezf.mimo.gruposcaminosantiago.presentation.ui.activity.MapsActivity
import mperezf.mimo.gruposcaminosantiago.presentation.viewModel.AddGroupViewModel
import java.util.*


class AddGroupFragment : BaseFragment() {

    companion object {

        const val DEPARTURE_DIALOG_TAG: String = "departure_dialog"
        const val ARRIVAL_DIALOG_TAG: String = "arrival_dialog"

        private const val PERMISSION_REQUEST_CODE = 1
        private const val GALLERY_REQUEST_CODE = 2
        private const val REQUEST_LOCATION: Int = 3
        private const val DATE_PATTERN = "HH:mm dd/MM/yy"

        fun newInstance() = AddGroupFragment()
    }

    private lateinit var viewModel: AddGroupViewModel
    private lateinit var departureDialogFragment: SwitchDateTimeDialogFragment
    private lateinit var arrivalDialogFragment: SwitchDateTimeDialogFragment
    private lateinit var etDepartureDate: TextView
    private lateinit var etArrivalDate: TextView

    private var departureDate: Date = Date()
    private var arrivalDate: Date = Date()
    private var groupLat: Double? = 0.0
    private var groupLng: Double? = 0.0

    private var addGroupListener: AddGroupListener? = null
    private var bitmapGroupImage: Bitmap? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.add_group_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        etDepartureDate = view.findViewById(R.id.et_departure_date_value)
        etArrivalDate = view.findViewById(R.id.et_arrival_date_value)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AddGroupViewModel::class.java)

        addListeners()

        addObservers()

        initDateTimeDialogs()
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
                    groupLat = data?.getDoubleExtra(MapsActivity.MAP_LAT, 0.0)
                    groupLng = data?.getDoubleExtra(MapsActivity.MAP_LNG, 0.0)
                    val coordinates = String.format("Lat: %.3f", groupLat) + " Lng:" + String.format("%.3f", groupLng)
                    et_departure_coordinates.setText(coordinates)
                }
            }
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        addGroupListener = activity as AddGroupListener
    }

    override fun onDetach() {
        addGroupListener = null
        super.onDetach()
    }


    private fun addObservers() {
        viewModel.getErrorMsg().observe(this, Observer<String> {
            showMessage(it)
        })

        viewModel.getLoadingState().observe(this, Observer<Boolean> {
            if (it) {
                bt_add_group.startAnimation()
            } else {
                bt_add_group.revertAnimation()
            }
        })

        viewModel.getGroupAdd().observe(this, Observer<Group> {
            addGroupListener?.groupAdded(it)
        })

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

        bt_add_group.setOnClickListener {
            closeKeyboard()
            if (validateForm()) {
                val newGroup = Group(
                    photo = bitmapGroupImage?.resize(500, 500)?.toBase64(),
                    title = et_group_title.text.toString(),
                    description = et_group_description.text.toString(),
                    departureDate = departureDate.time / 1000,
                    arrivalDate = arrivalDate.time / 1000,
                    departurePlace = et_departure_place.text.toString(),
                    latitude = groupLat,
                    longitude = groupLng
                )
                viewModel.createGroup(newGroup)
            }
        }
    }

    private fun validateForm(): Boolean {
        val validations = arrayOf(
            et_group_title.validate(true)
            , et_group_description.validate(true),
            et_departure_place.validate(true),
            et_departure_coordinates.validate(true)
        )

        val validDepartureDate = et_departure_date_value.text.isNotEmpty()
        if (!validDepartureDate) {
            et_departure_date.setTextColor(ContextCompat.getColor(context!!, R.color.error_color))
        } else {
            et_departure_date.setTextColor(ContextCompat.getColor(context!!, R.color.grey_2))
        }

        val validArrivalDate = et_arrival_date_value.text.isNotEmpty()
        if (!validArrivalDate) {
            et_arrival_date.setTextColor(ContextCompat.getColor(context!!, R.color.error_color))
        } else {
            et_arrival_date.setTextColor(ContextCompat.getColor(context!!, R.color.grey_2))
        }

        return !validations.contains(false) && validDepartureDate && validArrivalDate
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
                        value = date.time,
                        pattern = DATE_PATTERN,
                        unit = MILLIISECONDS
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
                        value = date.time,
                        pattern = DATE_PATTERN,
                        unit = MILLIISECONDS
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
