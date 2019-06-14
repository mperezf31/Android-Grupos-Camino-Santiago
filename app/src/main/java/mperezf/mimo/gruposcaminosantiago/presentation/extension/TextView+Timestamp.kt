package mperezf.mimo.gruposcaminosantiago.presentation.extension

import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*


fun TextView.fromTimestamp(value: Long, pattern: String = "dd/MM/yy' a las 'HH:mm") {

    text = try {
        val sdf = SimpleDateFormat(pattern, Locale("es", "ES"))
        sdf.format(Date(value * 1000))
    } catch (e: Exception) {
        ""
    }
}
