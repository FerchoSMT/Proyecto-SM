package com.psm.proyecto_sm.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.psm.proyecto_sm.R
import com.psm.proyecto_sm.adapters.RepliesAdapter
import com.psm.proyecto_sm.models.*
import com.psm.proyecto_sm.utils.DataManager
import com.psm.proyecto_sm.utils.DatabaseHelper
import com.psm.proyecto_sm.utils.ImageController
import com.psm.proyecto_sm.utils.NetworkConnection
import kotlinx.android.synthetic.main.activity_post.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class PostActivity : AppCompatActivity() {

    private lateinit var URL_READPOST : String
    private lateinit var URL_READPOSTREPLIES : String
    private lateinit var URL_DELETEPOST : String
    private lateinit var URL_DELETEREPLY : String
    private lateinit var URL_LIKEPOST : String
    val URL_CREATEREPLY = "http://cursoswelearn.xyz/phpApi/controllers/cCreateReply.php"

    var listReplies : MutableList<Reply> = mutableListOf()

    var idPost: Long? = null
    var postAux = Post()
    var replyAux = Reply()

    private lateinit var networkConnection: NetworkConnection

    private lateinit var recyclerViewReplies : RecyclerView
    private lateinit var repliesAdapter : RepliesAdapter
    private lateinit var builder : AlertDialog.Builder
    private lateinit var db : DatabaseHelper

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        iv_back_post.setOnClickListener{gobacktoMain()}
        iv_settings_post.setOnClickListener{
            if (DataManager.isConnected) {
                openSettings()
            }
            else {
                DataManager.connectionAlert(this)
            }
        }
        iv_like_post.setOnClickListener{
            if (DataManager.isConnected) {
                likePost()
            }
            else {
                DataManager.connectionAlert(this)
            }
        }
        iv_send_post.setOnClickListener{
            if (DataManager.isConnected) {
                sendReply()
            }
            else {
                DataManager.connectionAlert(this)
            }
        }

        db = DatabaseHelper(applicationContext)
        this.recyclerViewReplies = findViewById<RecyclerView>(R.id.rv_comments_post)

        val bundle : Bundle? = intent.extras
        idPost = bundle!!.getLong("IdPost")

        URL_READPOST = "http://cursoswelearn.xyz/phpApi/controllers/cReadPost.php?Id_Post=" + idPost
        URL_READPOSTREPLIES = "http://cursoswelearn.xyz/phpApi/controllers/cReadPostReplies.php?Id_Post=" + idPost
        URL_DELETEPOST = "http://cursoswelearn.xyz/phpApi/controllers/cDeletePost.php?Id_Post=" + idPost
        URL_LIKEPOST = "http://cursoswelearn.xyz/phpApi/controllers/cLikePost.php?Id_Post=" + idPost + "&Id_User=" + DataManager.userId

        getPost()

        networkConnection = NetworkConnection(application)
        networkConnection.observe(this) { isConnected ->
            DataManager.isConnected = isConnected
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setUpInfoPost() {
        val profilePicBmp = ImageController.getImageBitmap(postAux.img_user)
        if (profilePicBmp != null) {
            iv_pfp_post.setImageBitmap(profilePicBmp)
        }
        tv_username_post.setText(postAux.name_user)
        if (postAux.id_user != DataManager.userId) {
            iv_settings_post.visibility = View.GONE
        }
        tv_title_post.setText(postAux.title)
        tv_content_post.setText(postAux.content)
        if (postAux.imageA == null && postAux.imageB == null) {
            iv_image1_post.visibility = View.GONE
            iv_image2_post.visibility = View.GONE
        }
        else if (postAux.imageA != null && postAux.imageB == null) {
            iv_image1_post.setImageBitmap(ImageController.getImageBitmap(postAux.imageA))
            iv_image2_post.visibility = View.INVISIBLE
        }
        else if (postAux.imageA != null && postAux.imageB != null) {
            iv_image1_post.setImageBitmap(ImageController.getImageBitmap(postAux.imageA))
            iv_image2_post.setImageBitmap(ImageController.getImageBitmap(postAux.imageB))
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
                    URL_DELETEREPLY = "http://cursoswelearn.xyz/phpApi/controllers/cDeleteReply.php?Id_Reply=" + repliesParam[position].id_reply
                    repliesParam[position].delete(this@PostActivity, URL_DELETEREPLY)
                    repliesAdapter.deleteItem(repliesParam[position].id_reply!!)
                }
                builder.setNegativeButton("Cancelar") { dialogInterface, which -> }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.show()
            }
        })

        getPostReplies()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendReply() {
        replyAux.content = et_comment_post.text.toString()
        replyAux.id_user = DataManager.userId
        replyAux.id_post = idPost

        if (replyAux.content!!.isEmpty()) { et_comment_post.setError("Comentario vacío") }
        else {
            et_comment_post.setText("")

            replyAux.create(this, URL_CREATEREPLY)
            listReplies.clear()
            getPostReplies()
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
                        postAux.delete(this, URL_DELETEPOST)
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getPost() {

        var stringRequest = StringRequest(Request.Method.GET, URL_READPOST, { response ->

            try {
                var jsonObject = JSONObject(response)
                postAux.id_post = jsonObject.getLong("id_post")
                postAux.title = jsonObject.getString("title")
                postAux.content = jsonObject.getString("content")
                postAux.favorites = jsonObject.getInt("favorites")
                postAux.is_draft = jsonObject.getInt("is_draft") == 1
                postAux.posted_date = jsonObject.getString("posted_date")
                postAux.is_deleted = jsonObject.getInt("is_deleted") == 1
                postAux.id_user = jsonObject.getLong("id_user")

                val imageStrA = jsonObject.getString("imageA")
                if (imageStrA.isNotEmpty()) {
                    val imageA = imageStrA.replace("data:image/png;base64,", "")
                    postAux.imageA = Base64.getDecoder().decode(imageA)
                }

                val imageStrB = jsonObject.getString("imageB")
                if (imageStrB.isNotEmpty()) {
                    val imageB = imageStrB.replace("data:image/png;base64,", "")
                    postAux.imageB = Base64.getDecoder().decode(imageB)
                }

                postAux.name_user = jsonObject.getString("name_user")

                val imageStrC = jsonObject.getString("img_user")
                if (imageStrC.isNotEmpty()) {
                    val imageC = imageStrC.replace("data:image/png;base64,", "")
                    postAux.img_user = Base64.getDecoder().decode(imageC)
                }

            } catch (e: Exception) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
            } finally {
                setUpInfoPost()
            }

        }, { error ->
            Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
        })

        Volley.newRequestQueue(this).add(stringRequest)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getPostReplies() {

        var stringRequest = StringRequest(Request.Method.GET, URL_READPOSTREPLIES, { response ->

            try {
                var array = JSONArray(response)
                for (i in 0 until array.length()) {

                    var jsonObject = array.getJSONObject(i)

                    var replyAux = Reply()
                    replyAux.id_reply = jsonObject.getLong("id_reply")
                    replyAux.content = jsonObject.getString("content")
                    replyAux.replied_date = jsonObject.getString("replied_date")
                    replyAux.is_deleted = jsonObject.getInt("is_deleted") == 1
                    replyAux.id_user = jsonObject.getLong("id_user")
                    replyAux.id_post = jsonObject.getLong("id_post")
                    replyAux.name_user = jsonObject.getString("name_user")

                    val imageStr = jsonObject.getString("img_user")
                    if (imageStr.isNotEmpty()) {
                        val image = imageStr.replace("data:image/png;base64,", "")
                        replyAux.img_user = Base64.getDecoder().decode(image)
                    }

                    listReplies.add(replyAux)

                }

            } catch (e: Exception) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
            } finally {
                for (reply in listReplies) {
                    repliesAdapter.addItem(reply)
                }
            }

        }, { error ->
            Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
        })

        Volley.newRequestQueue(this).add(stringRequest)

    }

    private fun likePost() {

        var stringRequest = StringRequest(Request.Method.GET, URL_LIKEPOST, { response ->
            var likes: Long = -1;

            try {
                var jsonObject = JSONObject(response)
                likes = jsonObject.getLong("favorites")

            } catch (e: Exception) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
            } finally {
                if (likes >= 0) {
                    tv_likes_post.setText(likes.toString())
                }
                else {
                    Toast.makeText(this, "Error al dar like", Toast.LENGTH_LONG).show()
                }
            }

        }, { error ->
            Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
        })

        Volley.newRequestQueue(this).add(stringRequest)

    }
}