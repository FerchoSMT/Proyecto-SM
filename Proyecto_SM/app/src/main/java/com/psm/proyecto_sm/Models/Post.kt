package com.psm.proyecto_sm.Models

class Post : Content {
    var id_post: String = ""
    var title: String = ""
    var content: String = ""
    var favorites: Int = 0
    var is_draft: Boolean = false
    var posted_date: String = ""
    var id_user: String = ""

    override fun create() {
        TODO("Not yet implemented")
    }

    override fun update() {
        TODO("Not yet implemented")
    }

    override fun delete() {
        TODO("Not yet implemented")
    }

    fun saveDraft() {

    }

    fun seeReplies() {

    }

    fun addFavorite() {

    }

    fun search() {

    }
}

