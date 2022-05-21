package com.psm.proyecto_sm.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.psm.proyecto_sm.R
import com.psm.proyecto_sm.adapters.PostsAdapter
import com.psm.proyecto_sm.utils.DatabaseHelper
import com.psm.proyecto_sm.models.Post
import com.psm.proyecto_sm.utils.DataManager
import com.psm.proyecto_sm.utils.NetworkConnection
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import java.lang.Exception
import java.util.*

class MainActivity : AppCompatActivity() {

    val URL_READPOSTS = "http://cursoswelearn.xyz/phpApi/controllers/cReadPosts.php"

    var listPosts : MutableList<Post> = mutableListOf()

    private lateinit var networkConnection: NetworkConnection

    private lateinit var recyclerViewPosts : RecyclerView
    private lateinit var postsAdapter : PostsAdapter
    private lateinit var db : DatabaseHelper

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        iv_home_main.setOnClickListener { gotoMain()}
        iv_profile_main.setOnClickListener { gotoProfile()}
        iv_search_main.setOnClickListener { gotoSearch()}
        iv_publish_main.setOnClickListener {
            if (DataManager.isConnected) {
                gotoPublish()
            }
            else {
                DataManager.connectionAlert(this)
            }
        }

        db = DatabaseHelper(applicationContext)
        this.recyclerViewPosts = findViewById<RecyclerView>(R.id.rv_content_main)

        networkConnection = NetworkConnection(application)
        networkConnection.observe(this) { isConnected ->
            if (isConnected) {
                DataManager.isConnected = true
                getPosts()
            } else {
                DataManager.isConnected = false
            }
        }

        setUpRecyclerView()
    }

    private fun gotoMain() {
        //Aqui no va nada por que ya esta en Main o hacer funcion para recargar pagina o mandar arriba
    }

    private fun gotoProfile() {
        val intent = Intent(this, ProfileActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(intent)
        finish()
    }

    private fun gotoSearch() {
        val intent = Intent(this, SearchActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(intent)
        finish()
    }

    private fun gotoPublish() {
        val idZero: Long = 0
        val intent = Intent(this, PublishActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        intent.putExtra("IdPost", idZero)
        intent.putExtra("IsDraft", false)
        startActivity(intent)
        finish()
    }

    private fun setUpRecyclerView() {
        var postsParam : MutableList<Post> = mutableListOf()
        postsAdapter = PostsAdapter(postsParam)
        recyclerViewPosts.apply {
            adapter = postsAdapter
            layoutManager = LinearLayoutManager(context)
        }
        postsAdapter.setOnItemClickListener(object : PostsAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                val intent = Intent(this@MainActivity, PostActivity::class.java)
                intent.putExtra("IdPost", postsParam[position].id_post)
                startActivity(intent)
                finish()
            }
        })

        var listPostsLocal = db.readAllPosts()
        for (post in listPostsLocal) {
            if (post.is_draft == false && post.is_deleted == false) {
                postsAdapter.addItem(post)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        val intent = Intent(this, MainActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(intent)
        finish()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getPosts() {

        var stringRequest = StringRequest(Request.Method.GET, URL_READPOSTS, { response ->

            try {
                var array = JSONArray(response)
                for (i in 0 until array.length()) {

                    var jsonObject = array.getJSONObject(i)

                    var postAux = Post()
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

                    listPosts.add(postAux)

                }

            } catch (e: Exception) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
            } finally {
                db.getPostsFromDbHost(listPosts)
                for (post in listPosts) {
                    if (post.is_draft == false && post.is_deleted == false) {
                        postsAdapter.addItem(post)
                    }
                }
            }

        }, { error->
            Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
        })

        Volley.newRequestQueue(this).add(stringRequest)

    }
}