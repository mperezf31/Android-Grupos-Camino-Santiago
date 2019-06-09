package mperezf.mimo.gruposcaminosantiago.presentation.extension

import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*


fun TextView.fromTimestamp(value: Long, pattern: String = "dd/MM/yy' a las 'hh:mm") {

    text = try {
        val sdf = SimpleDateFormat(pattern, Locale("es", "ES"))
        val netDate = Date(value)
        sdf.format(netDate)
    } catch (e: Exception) {
        ""
    }
}
