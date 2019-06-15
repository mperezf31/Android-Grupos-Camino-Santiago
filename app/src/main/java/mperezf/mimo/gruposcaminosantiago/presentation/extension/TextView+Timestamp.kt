package mperezf.mimo.gruposcaminosantiago.presentation.extension

import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*


const val SECONDS = 1
const val MILLIISECONDS = 2

fun TextView.fromTimestamp(value: Long, pattern: String = "HH:mm dd/MM/yy", unit: Int = SECONDS) {

    text = try {
        var time = value
        if (unit == SECONDS) {
            time *= 1000
        }
        val sdf = SimpleDateFormat(pattern, Locale("es", "ES"))
        sdf.format(Date(time))
    } catch (e: Exception) {
        ""
    }
}
