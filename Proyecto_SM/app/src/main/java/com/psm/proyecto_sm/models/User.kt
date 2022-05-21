package com.psm.proyecto_sm.models

import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.psm.proyecto_sm.utils.DatabaseHelper
import com.psm.proyecto_sm.utils.DataManager
import com.psm.proyecto_sm.adapters.DraftsAdapter
import com.psm.proyecto_sm.adapters.PostsAdapter
import com.psm.proyecto_sm.adapters.RepliesAdapter
import com.psm.proyecto_sm.utils.DatabaseHost

class User {
    var id_user: Long? = null
    var name: String? = null
    var email: String? = null
    var password: String? = null
    var phone: String? = null
    var address: String? = null
    var profile_picture: ByteArray? = null
    var register_date: String? = null

    @RequiresApi(Build.VERSION_CODES.O)
    fun signUp(context: Context, url: String) {
        var db = DatabaseHost()
        db.createUser(context, url, this)
    }

    fun login(db: DatabaseHelper): Boolean {
        var userAux = db.loginUser(this)

        if (userAux.id_user != null) {
            DataManager.userId = userAux.id_user
            DataManager.userProfilePic = userAux.profile_picture
            return true
        }
        else {
            return false
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun update(context: Context, url: String) {
        var db = DatabaseHost()
        db.updateUser(context, url, this)
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


