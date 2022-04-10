package com.psm.proyecto_sm.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.psm.proyecto_sm.R
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        iv_edit_profile.setOnClickListener{gotoEdit()}
        iv_signout_profile.setOnClickListener{signOut()}
        txt_posts_profile.setOnClickListener{showPosts()}
        txt_replies_profile.setOnClickListener{showReplies()}
        txt_favorites_profile.setOnClickListener{showFavorites()}
        iv_main_profile.setOnClickListener{gotoMain()}
        iv_search_profile.setOnClickListener{gotoSearch()}
        iv_profile_profile.setOnClickListener{gotoProfile()}
    }

    private fun gotoProfile() {
        //Aqui nada o recargar perfil
    }

    private fun gotoSearch() {
        val intent = Intent(this, SearchActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(intent)
        finish()
    }

    private fun gotoMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(intent)
        finish()
    }

    private fun showFavorites() {
        TODO("Mostrar posts favoritos")
    }

    private fun showReplies() {
        TODO("Mostrar respuestas del usuario")
    }

    private fun showPosts() {
        TODO("Mostrar posts del usuario")
    }

    private fun signOut() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(intent)
        finish()
    }

    private fun gotoEdit() {
        val intent = Intent(this, EditActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(intent)
        finish()
    }
}