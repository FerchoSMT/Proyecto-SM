package com.psm.proyecto_sm.models

import android.content.Context
import com.psm.proyecto_sm.utils.DatabaseHost

class Reply : Content {
    var id_reply: Long? = null
    var content: String? = null
    var replied_date: String? = null
    var is_deleted: Boolean? = false
    var id_user: Long? = null
    var id_post: Long? = null

    var name_user: String? = null
    var img_user: ByteArray? = null

    override fun create(context: Context, url: String) {
        var db = DatabaseHost()
        db.createReply(context, url, this)
    }

    override fun update(context: Context, url: String) {
        //SE ELIMINO OPCION
    }

    override fun delete(context: Context, url: String) {
        var db = DatabaseHost()
        db.deleteReply(context, url)
    }
}