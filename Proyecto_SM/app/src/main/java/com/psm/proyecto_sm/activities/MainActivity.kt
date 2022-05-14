package com.psm.proyecto_sm.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.psm.proyecto_sm.R
import com.psm.proyecto_sm.adapters.PostsAdapter
import com.psm.proyecto_sm.models.DatabaseHelper
import com.psm.proyecto_sm.models.Post
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerViewPosts : RecyclerView
    private lateinit var postsAdapter : PostsAdapter
    private lateinit var db : DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        iv_home_main.setOnClickListener { gotoMain()}
        iv_profile_main.setOnClickListener { gotoProfile()}
        iv_search_main.setOnClickListener { gotoSearch()}
        iv_publish_main.setOnClickListener { gotoPublish()}

        db = DatabaseHelper(applicationContext)
        this.recyclerViewPosts = findViewById<RecyclerView>(R.id.rv_content_main)

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

        var listPosts = db.readAllPosts()
        for (post in listPosts) {
            postsAdapter.addItem(post)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        val intent = Intent(this, MainActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(intent)
        finish()
    }
}