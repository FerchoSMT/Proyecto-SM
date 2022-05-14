package com.psm.proyecto_sm.models

import com.psm.proyecto_sm.Utils.DatabaseHelper
import com.psm.proyecto_sm.Utils.UserLogged
import com.psm.proyecto_sm.adapters.DraftsAdapter
import com.psm.proyecto_sm.adapters.PostsAdapter
import com.psm.proyecto_sm.adapters.RepliesAdapter

class User {
    var id_user: Long? = null
    var name: String? = null
    var email: String? = null
    var password: String? = null
    var phone: String? = null
    var address: String? = null
    var profile_picture: ByteArray? = null
    var register_date: String? = null

    fun signUp(db: DatabaseHelper) {
        var user_id = db.createUser(this)

        UserLogged.userId = user_id
        UserLogged.userProfilePic = this.profile_picture
    }

    fun login(db: DatabaseHelper): Boolean {
        var userAux = db.loginUser(this)

        if (userAux.id_user != null) {
            UserLogged.userId = userAux.id_user
            UserLogged.userProfilePic = userAux.profile_picture
            return true
        }
        else {
            return false
        }
    }

    fun update(db: DatabaseHelper) {
        db.updateUser(this)

        UserLogged.userProfilePic = this.profile_picture
    }

    fun seePosts(db: DatabaseHelper, adapter: PostsAdapter) {
        var listPosts = db.readUserPosts(this.id_user!!)
        for (post in listPosts) {
            adapter.addItem(post)
        }
    }

    fun seeReplies(db: DatabaseHelper, adapter: RepliesAdapter) {
        var listReplies = db.readUserReplies(this.id_user!!)
        for (reply in listReplies) {
            adapter.addItem(reply)
        }
    }

    fun seeFavorites(db: DatabaseHelper, adapter: PostsAdapter) {
        var listPosts = db.readUserFavorites(this.id_user!!)
        for (post in listPosts) {
            adapter.addItem(post)
        }
    }

    fun seeDrafts(db: DatabaseHelper, adapter: DraftsAdapter) {
        var listDrafts = db.readUserDraftPosts(this.id_user!!)
        for (draft in listDrafts) {
            adapter.addItem(draft)
        }
    }
}


