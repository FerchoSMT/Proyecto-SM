package com.psm.proyecto_sm.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.psm.proyecto_sm.R
import kotlinx.android.synthetic.main.activity_post.*

class PostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        iv_back_post.setOnClickListener{gobacktoMain()}
        iv_home_post.setOnClickListener{gobacktoMain()}
        iv_search_post.setOnClickListener{gotoSearch()}
        iv_profile_post.setOnClickListener{gotoProfile()}
        iv_settings_post.setOnClickListener{openSettings()}
        iv_like_post.setOnClickListener{likePost()}
        iv_send_post.setOnClickListener{sendReply()}
    }

    private fun sendReply() {
        TODO("Funcion para comentar en el post")
    }

    private fun likePost() {
        TODO("Funcion para dar likes")
    }

    private fun openSettings() {
        TODO("Abrir cuadro de texto para escoger entre editar y borrar, si se presiona editar llamar a la funcion de editar que lleve a activity_publish con los datos del post")
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

    private fun gobacktoMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(intent)
        finish()
    }
}