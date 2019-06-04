package mperezf.mimo.gruposcaminosantiago.presentation.extension

import android.widget.ImageView
import android.graphics.BitmapFactory
import android.util.Base64


fun ImageView.fromBase64(data: String) {

    val imageAsBytes = Base64.decode(data, Base64.DEFAULT)
    setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.size))
}

