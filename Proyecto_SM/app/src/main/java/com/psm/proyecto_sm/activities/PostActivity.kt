package com.psm.proyecto_sm.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.psm.proyecto_sm.R
import com.psm.proyecto_sm.Utils.DatabaseHelper
import com.psm.proyecto_sm.Utils.ImageController
import com.psm.proyecto_sm.Utils.UserLogged
import com.psm.proyecto_sm.adapters.RepliesAdapter
import com.psm.proyecto_sm.models.*
import kotlinx.android.synthetic.main.activity_post.*
import java.lang.Exception

class PostActivity : AppCompatActivity() {

    var idPost: Long? = null
    var postAux = Post()
    var replyAux = Reply()

    private lateinit var recyclerViewReplies : RecyclerView
    private lateinit var repliesAdapter : RepliesAdapter
    private lateinit var builder : AlertDialog.Builder
    private lateinit var db : DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        iv_back_post.setOnClickListener{gobacktoMain()}
        iv_settings_post.setOnClickListener{openSettings()}
        iv_like_post.setOnClickListener{likePost()}
        iv_send_post.setOnClickListener{sendReply()}

        db = DatabaseHelper(applicationContext)
        this.recyclerViewReplies = findViewById<RecyclerView>(R.id.rv_comments_post)

        val bundle : Bundle? = intent.extras
        idPost = bundle!!.getLong("IdPost")

        setUpInfoPost()
    }

    private fun setUpInfoPost() {
        postAux = db.readPost(idPost!!)
        val profilePicBmp = ImageController.getImageBitmap(postAux.img_user)
        if (profilePicBmp != null) {
            iv_pfp_post.setImageBitmap(profilePicBmp)
        }
        tv_username_post.setText(postAux.name_user)
        if (postAux.id_user != UserLogged.userId) {
            iv_settings_post.visibility = View.GONE
        }
        tv_title_post.setText(postAux.title)
        tv_content_post.setText(postAux.content)
        if (postAux.images.size == 0) {
            iv_image1_post.visibility = View.GONE
            iv_image2_post.visibility = View.GONE
        }
        else if (postAux.images.size == 1) {
            iv_image1_post.setImageBitmap(ImageController.getImageBitmap(postAux.images[0]))
            iv_image2_post.visibility = View.INVISIBLE
        }
        else if (postAux.images.size == 2) {
            iv_image1_post.setImageBitmap(ImageController.getImageBitmap(postAux.images[0]))
            iv_image2_post.setImageBitmap(ImageController.getImageBitmap(postAux.images[1]))
        }
        tv_date_post.setText(postAux.posted_date)
        tv_likes_post.setText(postAux.favorites.toString())

        var repliesParam : MutableList<Reply> = mutableListOf()
        repliesAdapter = RepliesAdapter(repliesParam)
        recyclerViewReplies.apply {
            adapter = repliesAdapter
            layoutManager = LinearLayoutManager(context)
        }
        repliesAdapter.setOnItemClickListener(object : RepliesAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {  }
            override fun onSettingsClick(view: View, position: Int) {
                builder = AlertDialog.Builder(this@PostActivity)
                builder.setMessage("Esta acción no se puede deshacer")
                builder.setPositiveButton("Eliminar") { dialogInterface, which ->
                    repliesParam[position].delete(db)
                    repliesAdapter.deleteItem(repliesParam[position].id_reply!!)
                }
                builder.setNegativeButton("Cancelar") { dialogInterface, which -> }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.show()
            }
        })

        postAux.getReplies(db, repliesAdapter)
    }

    private fun sendReply() {
        replyAux.content = et_comment_post.text.toString()
        replyAux.id_user = UserLogged.userId
        replyAux.id_post = idPost

        if (replyAux.content!!.isEmpty()) { et_comment_post.setError("Comentario vacío") }
        else {
            et_comment_post.setText("")

            replyAux.create(db)
            postAux.getReplies(db, repliesAdapter)
        }
    }

    private fun likePost() {
        val postFavs = tv_likes_post.text.toString().toInt()

        if (db.favePost(UserLogged.userId!!, postAux.id_post!!)) {
            var num = postFavs + 1
            tv_likes_post.setText(num.toString())
        }
        else {
            var num = postFavs - 1
            tv_likes_post.setText(num.toString())
        }
    }

    private fun openSettings() {
        val popupMenu = PopupMenu(this, iv_settings_post)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_edit -> {
                    val intent = Intent(this, PublishActivity::class.java)
                    intent.putExtra("IdPost", idPost)
                    intent.putExtra("IsDraft", false)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.menu_delete -> {
                    builder = AlertDialog.Builder(this)
                    builder.setMessage("Esta acción no se puede deshacer")
                    builder.setPositiveButton("Eliminar") { dialogInterface, which ->
                        postAux.delete(db)
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    builder.setNegativeButton("Cancelar") { dialogInterface, which -> }
                    val alertDialog: AlertDialog = builder.create()
                    alertDialog.show()
                    true
                }
                else -> false
            }
        }

        popupMenu.inflate(R.menu.options_menu)
        try {
            val fieldMPopup = PopupMenu::class.java.getDeclaredField("mPopup")
            fieldMPopup.isAccessible = true
            val mPopup = fieldMPopup.get(popupMenu)
            mPopup.javaClass
                .getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                .invoke(mPopup, true)
        } catch (e: Exception) {
            Log.e("Menu", "Error mostrando iconos", e)
        } finally {
            popupMenu.show()
        }
    }

    private fun gobacktoMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()

        val intent = Intent(this, MainActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(intent)
        finish()
    }
}