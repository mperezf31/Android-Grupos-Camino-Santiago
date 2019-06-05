package mperezf.mimo.gruposcaminosantiago.presentation.extension

import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*


fun TextView.fromTimestamp(value: Int, pattern: String) {

    text = try {
        val sdf = SimpleDateFormat(pattern, Locale("es", "ES"))
        val netDate = Date(value.toLong())
        sdf.format(netDate)
    } catch (e: Exception) {
        ""
    }
}

