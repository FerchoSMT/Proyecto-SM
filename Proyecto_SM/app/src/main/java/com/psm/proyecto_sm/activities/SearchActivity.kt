package com.psm.proyecto_sm.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.psm.proyecto_sm.R
import com.psm.proyecto_sm.adapters.PostsAdapter
import com.psm.proyecto_sm.models.DatabaseHelper
import com.psm.proyecto_sm.models.Post
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {

    private lateinit var recyclerViewPosts : RecyclerView
    private lateinit var postsAdapter : PostsAdapter
    private lateinit var db : DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        iv_home_search.setOnClickListener{gotoMain()}
        iv_search_search.setOnClickListener{gotoSearch()}
        iv_profile_search.setOnClickListener{gotoProfile()}
        iv_searchicon_search.setOnClickListener{searchForResults()}

        db = DatabaseHelper(applicationContext)
        this.recyclerViewPosts = findViewById<RecyclerView>(R.id.rv_results_search)

        var postsParam : MutableList<Post> = mutableListOf()
        postsAdapter = PostsAdapter(postsParam)
        recyclerViewPosts.apply {
            adapter = postsAdapter
            layoutManager = LinearLayoutManager(context)
        }
        postsAdapter.setOnItemClickListener(object : PostsAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                val intent = Intent(this@SearchActivity, PostActivity::class.java)
                intent.putExtra("IdPost", postsParam[position].id_post)
                startActivity(intent)
                finish()
            }
        })
    }

    private fun searchForResults() {
        var search = et_searchbox_search.text.toString()

        if (search.isEmpty()) { et_searchbox_search.setError("Búsqueda vacía") }
        else {
            postsAdapter.clearItems()

            var listPosts = db.searchPosts(search)
            for (post in listPosts) {
                postsAdapter.addItem(post)
            }
        }
    }

    private fun gotoProfile() {
        val intent = Intent(this, ProfileActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(intent)
        finish()
    }

    private fun gotoSearch() {
        //"Aqui no va nada o recargar pagina
    }

    private fun gotoMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(intent)
        finish()
    }
}