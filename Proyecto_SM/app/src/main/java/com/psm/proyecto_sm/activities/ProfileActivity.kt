package com.psm.proyecto_sm.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.psm.proyecto_sm.utils.DatabaseHelper
import com.psm.proyecto_sm.utils.ImageController
import com.psm.proyecto_sm.models.User
import com.psm.proyecto_sm.utils.DataManager
import com.psm.proyecto_sm.fragments.ProfileFavorites
import com.psm.proyecto_sm.fragments.ProfilePosts
import com.psm.proyecto_sm.fragments.ProfileReplies
import com.psm.proyecto_sm.R
import com.psm.proyecto_sm.databinding.ActivityMainBinding
import com.psm.proyecto_sm.models.Favorites
import com.psm.proyecto_sm.models.Post
import com.psm.proyecto_sm.models.Reply
import com.psm.proyecto_sm.utils.NetworkConnection
import kotlinx.android.synthetic.main.activity_profile.*
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception
import java.util.*

class ProfileActivity : AppCompatActivity() {

    val URL_READUSERS = "http://cursoswelearn.xyz/phpApi/controllers/cReadUsers.php"
    val URL_READPOSTS = "http://cursoswelearn.xyz/phpApi/controllers/cReadPosts.php"
    val URL_READREPLIES = "http://cursoswelearn.xyz/phpApi/controllers/cReadReplies.php"
    val URL_READFAVS = "http://cursoswelearn.xyz/phpApi/controllers/cReadFavorites.php"

    var listUsers : MutableList<User> = mutableListOf()
    var listPosts : MutableList<Post> = mutableListOf()
    var listReplies : MutableList<Reply> = mutableListOf()
    var listFavs : MutableList<Favorites> = mutableListOf()

    var userAux = User()

    private lateinit var networkConnection: NetworkConnection

    private lateinit var db : DatabaseHelper
    lateinit var binding: ActivityMainBinding

    @RequiresApi(Build.VERSION_CODES.O)
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

        iv_edit_profile.setOnClickListener{
            if (DataManager.isConnected) {
                gotoEdit()
            }
            else {
                DataManager.connectionAlert(this)
            }
        }
        iv_signout_profile.setOnClickListener{signOut()}

        iv_main_profile.setOnClickListener{gotoMain()}
        iv_search_profile.setOnClickListener{gotoSearch()}
        iv_profile_profile.setOnClickListener{gotoProfile()}

        db = DatabaseHelper(applicationContext)

        networkConnection = NetworkConnection(application)
        networkConnection.observe(this) { isConnected ->
            if (isConnected) {
                DataManager.isConnected = true
                getUsers()
                getPosts()
                getReplies()
                getFavorites()
            }
            else {
                DataManager.isConnected = false
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

    private fun signOut() {
        DataManager.userId = null
        DataManager.userProfilePic = null

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

    override fun onBackPressed() {
        super.onBackPressed()

        val intent = Intent(this, MainActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(intent)
        finish()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getUsers() {

        var stringRequest = StringRequest(Request.Method.GET, URL_READUSERS, { response ->

            try {
                var array = JSONArray(response)
                for (i in 0 until array.length()) {

                    var jsonObject = array.getJSONObject(i)

                    var user = User()
                    user.id_user = jsonObject.getLong("id_user")
                    user.name = jsonObject.getString("name")
                    user.email = jsonObject.getString("email")
                    user.password = jsonObject.getString("password")
                    user.phone = jsonObject.getString("phone")
                    user.address = jsonObject.getString("address")

                    val imageStr = jsonObject.getString("profile_picture")
                    if (imageStr.isNotEmpty()) {
                        val image = imageStr.replace("data:image/png;base64,", "")
                        user.profile_picture = Base64.getDecoder().decode(image)
                    }

                    user.register_date = jsonObject.getString("register_date")

                    listUsers.add(user)
                }

            } catch (e : Exception) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
            } finally {
                db.getUsersFromDbHost(listUsers)

                if (DataManager.userId != null) {
                    userAux = db.readUser(DataManager.userId!!)
                    txt_name_profile.setText(userAux.name)
                    txt_email_profile.setText(userAux.email)
                    val profilePicBmp = ImageController.getImageBitmap(userAux.profile_picture)
                    if (profilePicBmp != null) {
                        iv_pfpic_profile.setImageBitmap(profilePicBmp)
                    }
                }
            }

        }, { error ->
            Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
        })

        Volley.newRequestQueue(this).add(stringRequest)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getPosts() {

        var stringRequest = StringRequest(Request.Method.GET, URL_READPOSTS, { response ->

            try {
                var array = JSONArray(response)
                for (i in 0 until array.length()) {

                    var jsonObject = array.getJSONObject(i)

                    var postAux = Post()
                    postAux.id_post = jsonObject.getLong("id_post")
                    postAux.title = jsonObject.getString("title")
                    postAux.content = jsonObject.getString("content")
                    postAux.favorites = jsonObject.getInt("favorites")
                    postAux.is_draft = jsonObject.getInt("is_draft") == 1
                    postAux.posted_date = jsonObject.getString("posted_date")
                    postAux.is_deleted = jsonObject.getInt("is_deleted") == 1
                    postAux.id_user = jsonObject.getLong("id_user")

                    val imageStrA = jsonObject.getString("imageA")
                    if (imageStrA.isNotEmpty()) {
                        val imageA = imageStrA.replace("data:image/png;base64,", "")
                        postAux.imageA = Base64.getDecoder().decode(imageA)
                    }

                    val imageStrB = jsonObject.getString("imageB")
                    if (imageStrB.isNotEmpty()) {
                        val imageB = imageStrB.replace("data:image/png;base64,", "")
                        postAux.imageB = Base64.getDecoder().decode(imageB)
                    }

                    postAux.name_user = jsonObject.getString("name_user")

                    val imageStrC = jsonObject.getString("img_user")
                    if (imageStrC.isNotEmpty()) {
                        val imageC = imageStrC.replace("data:image/png;base64,", "")
                        postAux.img_user = Base64.getDecoder().decode(imageC)
                    }

                    listPosts.add(postAux)

                }

            } catch (e : Exception) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
            } finally {
                db.getPostsFromDbHost(listPosts)
            }

        }, { error ->
            Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
        })

        Volley.newRequestQueue(this).add(stringRequest)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getReplies() {

        var stringRequest = StringRequest(Request.Method.GET, URL_READREPLIES, { response ->

            try {
                var array = JSONArray(response)
                for (i in 0 until array.length()) {

                    var jsonObject = array.getJSONObject(i)

                    var replyAux = Reply()
                    replyAux.id_reply = jsonObject.getLong("id_reply")
                    replyAux.content = jsonObject.getString("content")
                    replyAux.replied_date = jsonObject.getString("replied_date")
                    replyAux.is_deleted = jsonObject.getInt("is_deleted") == 1
                    replyAux.id_user = jsonObject.getLong("id_user")
                    replyAux.id_post = jsonObject.getLong("id_post")
                    replyAux.name_user = jsonObject.getString("name_user")

                    val imageStr = jsonObject.getString("img_user")
                    if (imageStr.isNotEmpty()) {
                        val image = imageStr.replace("data:image/png;base64,", "")
                        replyAux.img_user = Base64.getDecoder().decode(image)
                    }

                    listReplies.add(replyAux)

                }

            } catch (e: Exception) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
            } finally {
                db.getRepliesFromDbHost(listReplies)
            }

        }, { error ->
            Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
        })

        Volley.newRequestQueue(this).add(stringRequest)

    }

    private fun getFavorites() {

        var stringRequest = StringRequest(Request.Method.GET, URL_READFAVS, { response ->

            try {
                var array = JSONArray(response)
                for (i in 0 until array.length()) {

                    var jsonObject = array.getJSONObject(i)

                    var favAux = Favorites()
                    favAux.id_fav = jsonObject.getLong("id_fav")
                    favAux.favorite = jsonObject.getInt("favorite") == 1
                    favAux.id_user = jsonObject.getLong("id_user")
                    favAux.id_post = jsonObject.getLong("id_post")

                    listFavs.add(favAux)
                }

            } catch (e: Exception) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
            } finally {
                db.getFavoritesFromDbHost(listFavs)
            }

        }, { error ->
            Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
        })

        Volley.newRequestQueue(this).add(stringRequest)

    }
}