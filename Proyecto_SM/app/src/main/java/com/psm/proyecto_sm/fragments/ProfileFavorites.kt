package com.psm.proyecto_sm.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.psm.proyecto_sm.R
import com.psm.proyecto_sm.activities.PostActivity
import com.psm.proyecto_sm.adapters.PostsAdapter
import com.psm.proyecto_sm.models.DatabaseHelper
import com.psm.proyecto_sm.models.Post
import com.psm.proyecto_sm.models.User
import com.psm.proyecto_sm.models.UserLogged


class ProfileFavorites : Fragment() {

    private lateinit var recyclerViewUserFavorites : RecyclerView
    private lateinit var postsAdapter : PostsAdapter
    private lateinit var db : DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_profile_favorites, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = DatabaseHelper(activity?.applicationContext)

        this.recyclerViewUserFavorites = view.findViewById<RecyclerView>(R.id.recyclerView)
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        var postsParam : MutableList<Post> = mutableListOf()
        postsAdapter = PostsAdapter(postsParam)
        recyclerViewUserFavorites.apply {
            adapter = postsAdapter
            layoutManager = LinearLayoutManager(context)
        }
        postsAdapter.setOnItemClickListener(object : PostsAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                val intent = Intent(activity, PostActivity::class.java)
                intent.putExtra("IdPost", postsParam[position].id_post)
                startActivity(intent)
            }
        })

        var userAux = User()
        userAux.id_user = UserLogged.userId

        userAux.seeFavorites(db, postsAdapter)
    }
}