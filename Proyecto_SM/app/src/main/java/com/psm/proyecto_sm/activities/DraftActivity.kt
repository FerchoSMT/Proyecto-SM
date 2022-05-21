package com.psm.proyecto_sm.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.psm.proyecto_sm.R
import com.psm.proyecto_sm.adapters.DraftsAdapter
import com.psm.proyecto_sm.utils.DatabaseHelper
import com.psm.proyecto_sm.models.Post
import com.psm.proyecto_sm.models.User
import com.psm.proyecto_sm.utils.DataManager
import kotlinx.android.synthetic.main.activity_draft.*

class DraftActivity : AppCompatActivity() {

    private lateinit var recyclerViewDrafts : RecyclerView
    private lateinit var draftsAdapter : DraftsAdapter
    private lateinit var db : DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_draft)

        iv_back_draft.setOnClickListener{gobacktoMain()}

        db = DatabaseHelper(applicationContext)
        this.recyclerViewDrafts = findViewById<RecyclerView>(R.id.rv_draft)

        setUpRecyclerView()
    }

    private fun gobacktoMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(intent)
        finish()
    }

    private fun setUpRecyclerView() {
        var draftsParam : MutableList<Post> = mutableListOf()
        draftsAdapter = DraftsAdapter(draftsParam)
        recyclerViewDrafts.apply {
            adapter = draftsAdapter
            layoutManager = LinearLayoutManager(context)
        }
        draftsAdapter.setOnItemClickListener(object : DraftsAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                val intent = Intent(this@DraftActivity, PublishActivity::class.java)
                intent.putExtra("IdPost", draftsParam[position].id_post)
                intent.putExtra("IsDraft", true)
                startActivity(intent)
                finish()
            }
        })

        var userAux = User()
        userAux.id_user = DataManager.userId

        userAux.seeDrafts(db, draftsAdapter)
    }
}