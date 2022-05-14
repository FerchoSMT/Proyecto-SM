package com.psm.proyecto_sm.Utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
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
            if (post.images.size < 1) { post.images.add(bytes) }
            else { post.images[0] = bytes }
        }
    }

    fun savePostImage2(context: Context, post: Post, uri: Uri) {
        val bytes = context.contentResolver.openInputStream(uri)?.readBytes()

        if (bytes != null) {
            if (post.images.size < 2) { post.images.add(bytes) }
            else { post.images[1] = bytes }
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