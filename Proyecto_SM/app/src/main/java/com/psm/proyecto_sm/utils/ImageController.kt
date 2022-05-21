package com.psm.proyecto_sm.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import com.psm.proyecto_sm.models.Post
import com.psm.proyecto_sm.models.User

object ImageController {

    fun selectPhotoFromGallery(activity: Activity, code: Int) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        activity.startActivityForResult(intent, code)
    }

    fun saveImageUser(context: Context, user: User, uri: Uri) {
        val bytes = context.contentResolver.openInputStream(uri)?.readBytes()

        if (bytes != null) {
            user.profile_picture = bytes
        }
    }

    fun savePostImage1(context: Context, post: Post, uri: Uri) {
        val bytes = context.contentResolver.openInputStream(uri)?.readBytes()

        if (bytes != null) {
            post.imageA = bytes
        }
    }

    fun savePostImage2(context: Context, post: Post, uri: Uri) {
        val bytes = context.contentResolver.openInputStream(uri)?.readBytes()

        if (bytes != null) {
            post.imageB = bytes
        }
    }

    fun getImageBitmap(img: ByteArray?): Bitmap? {
        if (img != null) {
            var bmp = BitmapFactory.decodeByteArray(img, 0, img.size)
            return bmp
        } else {
            return null
        }
    }
}