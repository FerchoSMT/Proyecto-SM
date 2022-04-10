package com.psm.proyecto_sm.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.psm.proyecto_sm.R
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        iv_home_search.setOnClickListener{gotoMain()}
        iv_search_search.setOnClickListener{gotoSearch()}
        iv_searchicon_search.setOnClickListener{searchforResults()}
        iv_profile_search.setOnClickListener{gotoProfile()}
    }

    private fun gotoProfile() {
        val intent = Intent(this, ProfileActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(intent)
        finish()
    }

    private fun searchforResults() {
        TODO("Funcion para buscar lo que este en la searchbar")
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