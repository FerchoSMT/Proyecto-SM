package com.psm.proyecto_sm.models

class Reply : Content {
    var id_reply: Long? = null
    var content: String? = null
    var replied_date: String? = null
    var id_user: Long? = null
    var id_post: Long? = null

    var name_user: String? = null
    var img_user: ByteArray? = null

    override fun create(db: DatabaseHelper) : Long {
        var reply_id = db.createReply(this)

        return reply_id
    }

    override fun update(db: DatabaseHelper) {
        db.updateReply(this)
    }

    override fun delete(db: DatabaseHelper) {
        db.deleteReply(this)
    }
}