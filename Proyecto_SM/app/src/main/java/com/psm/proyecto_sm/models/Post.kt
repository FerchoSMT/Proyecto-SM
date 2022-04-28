package com.psm.proyecto_sm.models

import com.psm.proyecto_sm.adapters.RepliesAdapter

class Post : Content {
    var id_post: Long? = null
    var title: String? = null
    var content: String? = null
    var favorites: Int? = null
    var is_draft: Boolean? = false
    var posted_date: String? = null
    var id_user: Long? = null

    var images: MutableList<ByteArray> = mutableListOf()

    var name_user: String? = null
    var img_user: ByteArray? = null

    override fun create(db: DatabaseHelper) : Long {
        var post_id = db.createPost(this)

        return post_id
    }

    override fun update(db: DatabaseHelper) {
        db.updatePost(this)
    }

    override fun delete(db: DatabaseHelper) {
        db.deletePost(this)
    }

    fun saveDraft(db: DatabaseHelper) {
        db.createDraftPost(this)
    }

    fun updateDraft(db: DatabaseHelper) {
        db.updateDraftPost(this)
    }

    fun getReplies(db: DatabaseHelper, adapter: RepliesAdapter) {
        var listReplies = db.readPostReplies(this.id_post!!)
        for (reply in listReplies) {
            adapter.addItem(reply)
        }
    }
}

