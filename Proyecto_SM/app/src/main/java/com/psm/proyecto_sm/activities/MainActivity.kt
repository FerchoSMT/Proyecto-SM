package com.psm.proyecto_sm.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.psm.proyecto_sm.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        iv_home_main.setOnClickListener { gotoMain()}
        iv_profile_main.setOnClickListener { gotoProfile()}
        iv_search_main.setOnClickListener { gotoSearch()}
        iv_publish_main.setOnClickListener { gotoPublish()}
    }

    private fun gotoPublish() {
        val intent = Intent(this, PublishActivity::class.java)
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

    private fun gotoProfile() {
        val intent = Intent(this, ProfileActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(intent)
        finish()
    }

    private fun gotoMain() {
        //Aqui no va nada por que ya esta en Main o hacer funcion para recargar pagina o mandar arriba
    }
}