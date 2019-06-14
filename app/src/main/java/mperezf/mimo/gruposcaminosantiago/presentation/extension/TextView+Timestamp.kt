package mperezf.mimo.gruposcaminosantiago.presentation.extension

import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*


fun TextView.fromTimestamp(value: Long, pattern: String = "HH:mm dd/MM/yy") {

    text = try {
        val sdf = SimpleDateFormat(pattern, Locale("es", "ES"))
        sdf.format(Date(value * 1000))
    } catch (e: Exception) {
        ""
    }
}
