package com.lab49.taptosnap.util

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore.Images
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.text.DecimalFormat
import java.util.*


fun formatTimeForTimer(millisUntilFinished: Long): String {
    val f = DecimalFormat("00")
    val hour = (millisUntilFinished / 3600000) % 24
    val min = (millisUntilFinished / 60000) % 60
    val sec = (millisUntilFinished / 1000) % 60
    return "${f.format(hour)}:${f.format(min)}:${f.format(sec)}"
}

fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
    val bytes = ByteArrayOutputStream()
    inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
    val path = Images.Media.insertImage(inContext.contentResolver, inImage, "IMG_" + Calendar.getInstance().getTime(), null)
    return Uri.parse(path)
}

fun convertToBase64(context: Context, uri: Uri?): String? {
    var sImage: String? = null
    try {
        val bitmap = Images.Media.getBitmap(context.contentResolver, uri)
        // initialize byte stream
        val stream = ByteArrayOutputStream()
        // compress Bitmap
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        // Initialize byte array
        val bytes = stream.toByteArray()
        // get base64 encoded string

        sImage = Base64.getEncoder().encodeToString(bytes)
        // set encoded text on textview
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return sImage
}
