package com.psm.proyecto_sm.Models

class User {
    var id_user: Long? = null
    var name: String? = null
    var email: String? = null
    var password: String? = null
    var phone: Int? = null
    var address: String? = null
    var profile_picture: ByteArray? = null
    var register_date: String? = null

    fun signUp(db: DatabaseHelper) {
        var user_id = db.createUser(this)

        UserLogged.userId = user_id
    }

    fun seePosts() {

    }

    fun seeReplies() {

    }

    fun seeFavorites() {

    }

    fun update() {

    }
}


