package mperezf.mimo.gruposcaminosantiago.presentation.extension

import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*


fun TextView.fromTimestamp(value: Long, pattern: String = "dd/MM/yy' a las 'HH:mm") {

    text = try {
        val sdf = SimpleDateFormat(pattern, Locale("es", "ES"))
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        sdf.format( Date(value))
    } catch (e: Exception) {
        ""
    }
}
