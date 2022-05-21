package com.psm.proyecto_sm.models

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.psm.proyecto_sm.utils.DatabaseHelper
import com.psm.proyecto_sm.adapters.RepliesAdapter
import com.psm.proyecto_sm.utils.DatabaseHost

class Post : Content {
    var id_post: Long? = null
    var title: String? = null
    var content: String? = null
    var favorites: Int? = null
    var is_draft: Boolean? = false
    var posted_date: String? = null
    var is_deleted: Boolean? = false
    var id_user: Long? = null

    var imageA: ByteArray? = null
    var imageB: ByteArray? = null

    var name_user: String? = null
    var img_user: ByteArray? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun create(context: Context, url: String) {
        var db = DatabaseHost()
        db.createPost(context, url, this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun update(context: Context, url: String) {
        var db = DatabaseHost()
        db.updatePost(context, url, this)
    }

    override fun delete(context: Context, url: String) {
        var db = DatabaseHost()
        db.deletePost(context, url)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun saveDraft(context: Context, url: String) {
        var db = DatabaseHost()
        db.saveDraft(context, url, this)
    }

    fun updateDraft(context: Context, url: String, idPost: Long) {
        var db = DatabaseHost()
        db.updateDraft(context, url, idPost)
    }

}

