package mperezf.mimo.gruposcaminosantiago.presentation.extension

import android.text.InputType
import android.widget.EditText
import androidx.core.content.ContextCompat
import mperezf.mimo.gruposcaminosantiago.R

fun EditText.validate(required: Boolean, inputType: Int = InputType.TYPE_CLASS_TEXT): Boolean {
    return if (required && text.isEmpty()) {
        setHintTextColor(ContextCompat.getColor(context, R.color.error_color))
        false
    } else {
        setHintTextColor(ContextCompat.getColor(context,R.color.grey_2))
        return when (inputType) {
            InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS -> {
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
                    setTextColor(ContextCompat.getColor(context,R.color.white))
                    true
                } else {
                    setTextColor(ContextCompat.getColor(context,R.color.error_color))
                    false
                }
            }
            else -> {
                setTextColor(ContextCompat.getColor(context,R.color.white))
                true
            }
        }

    }
}