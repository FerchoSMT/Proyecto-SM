package com.psm.proyecto_sm.Models

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri

object ImageController {
    fun selectPhotoFromGallery(activity: Activity, code: Int) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        activity.startActivityForResult(intent, code)
    }

    fun saveImage(context: Context, user: User, uri: Uri) {
        val bytes = context.contentResolver.openInputStream(uri)?.readBytes()

        if (bytes != null) {
            user.profile_picture = bytes
        }
    }

    fun getImageBitmap(user: User): Bitmap? {
        if (user.profile_picture != null) {
            var bmp = BitmapFactory.decodeByteArray(user.profile_picture, 0, user.profile_picture!!.size)
            return bmp
        } else {
            return null
        }
    }
}