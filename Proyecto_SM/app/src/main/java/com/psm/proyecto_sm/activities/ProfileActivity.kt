package com.psm.proyecto_sm.activities

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputBinding
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.psm.proyecto_sm.Models.DatabaseHelper
import com.psm.proyecto_sm.Models.ImageController
import com.psm.proyecto_sm.Models.User
import com.psm.proyecto_sm.Models.UserLogged
import com.psm.proyecto_sm.ProfileFavorites
import com.psm.proyecto_sm.ProfilePosts
import com.psm.proyecto_sm.ProfileReplies
import com.psm.proyecto_sm.R
import com.psm.proyecto_sm.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {

    var userAux = User()

    private lateinit var db : DatabaseHelper
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        binding = ActivityMainBinding.inflate(layoutInflater);
        //setContentView(binding.root);


        txt_posts_profile.setOnClickListener{
            replaceFragment(ProfilePosts())
        }

        txt_favorites_profile.setOnClickListener{
            replaceFragment(ProfileFavorites())
        }
        txt_replies_profile.setOnClickListener{
            replaceFragment(ProfileReplies())
        }


        iv_edit_profile.setOnClickListener{gotoEdit()}
        iv_signout_profile.setOnClickListener{signOut()}

        iv_main_profile.setOnClickListener{gotoMain()}
        iv_search_profile.setOnClickListener{gotoSearch()}
        iv_profile_profile.setOnClickListener{gotoProfile()}

        db = DatabaseHelper(applicationContext)

        if (UserLogged.userId != null) {
            userAux = db.readUser(UserLogged.userId!!)
            txt_name_profile.setText(userAux.name)
            txt_email_profile.setText(userAux.email)
            val profilePicBmp = ImageController.getImageBitmap(userAux)
            if (profilePicBmp != null) {
                iv_pfpic_profile.setImageBitmap(profilePicBmp)
            }
        }
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer,fragment)
        fragmentTransaction.commit()
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