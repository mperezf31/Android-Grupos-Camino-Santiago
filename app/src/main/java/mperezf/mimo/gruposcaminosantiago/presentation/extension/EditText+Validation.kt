package mperezf.mimo.gruposcaminosantiago.presentation.extension

import android.text.InputType
import android.widget.EditText
import mperezf.mimo.gruposcaminosantiago.R

fun EditText.validate(required: Boolean, inputType: Int = InputType.TYPE_CLASS_TEXT): Boolean {
    return if (required && text.isEmpty()) {
        setHintTextColor(context.resources.getColor(R.color.error))
        false
    } else {
        setHintTextColor(context.resources.getColor(R.color.grey_2))
        return when (inputType) {
            InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS -> {
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
                    true
                } else {
                    setTextColor(context.resources.getColor(R.color.error))
                    false
                }
            }
            else -> {
                setTextColor(context.resources.getColor(R.color.grey_1))
                true
            }
        }

    }
}