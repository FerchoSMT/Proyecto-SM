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
import com.psm.proyecto_sm.adapters.RepliesAdapter
import com.psm.proyecto_sm.Utils.DatabaseHelper
import com.psm.proyecto_sm.models.Reply
import com.psm.proyecto_sm.models.User
import com.psm.proyecto_sm.Utils.UserLogged

class ProfileReplies : Fragment() {

    private lateinit var recyclerViewUserReplies : RecyclerView
    private lateinit var repliesAdapter : RepliesAdapter
    private lateinit var db : DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_profile_responses, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = DatabaseHelper(activity?.applicationContext)

        this.recyclerViewUserReplies = view.findViewById<RecyclerView>(R.id.recyclerView)
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        var repliesParam : MutableList<Reply> = mutableListOf()
        repliesAdapter = RepliesAdapter(repliesParam)
        recyclerViewUserReplies.apply {
            adapter = repliesAdapter
            layoutManager = LinearLayoutManager(context)
        }
        repliesAdapter.setOnItemClickListener(object : RepliesAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                val intent = Intent(activity, PostActivity::class.java)
                intent.putExtra("IdPost", repliesParam[position].id_post)
                startActivity(intent)
            }
            override fun onSettingsClick(view: View, position: Int) {  }
        })

        var userAux = User()
        userAux.id_user = UserLogged.userId

        userAux.seeReplies(db, repliesAdapter)
    }
}