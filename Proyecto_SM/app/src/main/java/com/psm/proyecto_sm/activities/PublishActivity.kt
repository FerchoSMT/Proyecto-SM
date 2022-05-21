package com.psm.proyecto_sm.activities

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.psm.proyecto_sm.R
import com.psm.proyecto_sm.utils.DatabaseHelper
import com.psm.proyecto_sm.utils.ImageController
import com.psm.proyecto_sm.models.Post
import com.psm.proyecto_sm.utils.DataManager
import com.psm.proyecto_sm.utils.NetworkConnection
import kotlinx.android.synthetic.main.activity_publish.*
import org.json.JSONObject
import java.lang.Exception
import java.util.*

class PublishActivity : AppCompatActivity() {

    val URL_CREATEPOST = "http://cursoswelearn.xyz/phpApi/controllers/cCreatePost.php"
    val URL_EDITPOST = "http://cursoswelearn.xyz/phpApi/controllers/cUpdatePost.php"
    val URL_SAVEDRAFT = "http://cursoswelearn.xyz/phpApi/controllers/cSaveDraft.php"
    private lateinit var URL_UPDATEDRAFT : String
    private lateinit var URL_READPOST : String

    val SELECT_ACTIVITY = 13
    var imageNum = 0
    var imageUri1: Uri? = null
    var imageUri2: Uri? = null

    var postAux = Post()
    var idPost : Long? = null
    var isDraft : Boolean? = null

    private lateinit var networkConnection: NetworkConnection

    private lateinit var progressDialog: ProgressDialog
    private lateinit var builder : AlertDialog.Builder
    private lateinit var db : DatabaseHelper

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_publish)

        txt_drafts.setOnClickListener { gotoDrafts() }
        iv_back_publish.setOnClickListener{gotoMain()}
        iv_confirm_publish.setOnClickListener{
            if (DataManager.isConnected) {
                postPost()
            }
            else {
                DataManager.connectionAlert(this)
            }
        }
        iv_image_publish.setOnClickListener{
            imageNum = 1
            selectImage()
        }
        iv_image_publish2.setOnClickListener {
            imageNum = 2
            selectImage()
        }
        iv_image_publish2.visibility = View.INVISIBLE

        db = DatabaseHelper(applicationContext)

        val bundle : Bundle? = intent.extras
        idPost = bundle!!.getLong("IdPost")
        isDraft = bundle!!.getBoolean("IsDraft")

        if (DataManager.userId != null) {
            val profilePicBmp = ImageController.getImageBitmap(DataManager.userProfilePic)
            if (profilePicBmp != null) {
                iv_pfpic_publish.setImageBitmap(profilePicBmp)
            }
        }

        if (idPost!!.toInt() != 0) {
            URL_READPOST = "http://cursoswelearn.xyz/phpApi/controllers/cReadPost.php?Id_Post=" + idPost
            URL_UPDATEDRAFT = "http://cursoswelearn.xyz/phpApi/controllers/cUpdateDraft.php?Id_Post=" + idPost
            getPost()
        }

        networkConnection = NetworkConnection(application)
        networkConnection.observe(this) { isConnected ->
            DataManager.isConnected = isConnected
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun postPost() {
        postAux.title = et_title_publish.text.toString()
        postAux.content = et_content_post.text.toString()
        postAux.id_user = DataManager.userId

        if (postAux.title!!.isEmpty()) { et_title_publish.setError("Título vacío") }
        else if (postAux.content!!.isEmpty()) { et_content_post.setError("Contenido vacío") }
        else {
            progressDialog = ProgressDialog(this)
            progressDialog.setMessage("Publicando...")
            progressDialog.show()

            imageUri1?.let {
                ImageController.savePostImage1(this, postAux, it)
            }
            imageUri2?.let {
                ImageController.savePostImage2(this, postAux, it)
            }

            if (isDraft == true) { postAux.updateDraft(this, URL_UPDATEDRAFT, idPost!!) }
            else {
                if (idPost!!.toInt() == 0) { postAux.create(this, URL_CREATEPOST) }
                else { postAux.update(this, URL_EDITPOST) }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun gotoMain() {
        if (idPost!!.toInt() == 0) {
            builder = AlertDialog.Builder(this)
            builder.setMessage("¿Guardar borrador?")
            builder.setPositiveButton("Guardar") { dialogInterface, which ->
                postAux.title = et_title_publish.text.toString()
                postAux.content = et_content_post.text.toString()
                postAux.id_user = DataManager.userId

                if (postAux.title!!.isEmpty()) { et_title_publish.setError("Título vacío") }
                else if (postAux.content!!.isEmpty()) { et_content_post.setError("Contenido vacío") }
                else {
                    imageUri1?.let {
                        ImageController.savePostImage1(this, postAux, it)
                    }
                    imageUri2?.let {
                        ImageController.savePostImage2(this, postAux, it)
                    }

                    postAux.saveDraft(this, URL_SAVEDRAFT)
                }
            }
            builder.setNegativeButton("Cancelar") { dialogInterface, which ->
                val intent = Intent(this, MainActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                startActivity(intent)
                finish()
            }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.show()
        }
        else {
            val intent = Intent(this, MainActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            startActivity(intent)
            finish()
        }
    }

    private fun gotoDrafts() {
        val intent = Intent(this, DraftActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(intent)
        finish()
    }

    private fun selectImage() {
        ImageController.selectPhotoFromGallery(this, SELECT_ACTIVITY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when {
            requestCode == SELECT_ACTIVITY && resultCode == Activity.RESULT_OK -> {
                if (imageNum == 1) {
                    imageUri1 = data!!.data
                    iv_image_publish.setImageURI(imageUri1)
                    iv_image_publish2.visibility = View.VISIBLE
                }
                else if (imageNum == 2){
                    imageUri2 = data!!.data
                    iv_image_publish2.setImageURI(imageUri2)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getPost() {

        var stringRequest = StringRequest(Request.Method.GET, URL_READPOST, { response ->

            try {
                var jsonObject = JSONObject(response)
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

            } catch (e: Exception) {

            } finally {
                txt_drafts.visibility = View.GONE
                et_title_publish.setText(postAux.title)
                et_content_post.setText(postAux.content)
                if (postAux.imageA != null) {
                    iv_image_publish.setImageBitmap(ImageController.getImageBitmap(postAux.imageA))
                    iv_image_publish2.visibility = View.VISIBLE
                }
                if (postAux.imageB != null) {
                    iv_image_publish2.setImageBitmap(ImageController.getImageBitmap(postAux.imageB))
                }
            }

        }, { error ->
            Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
        })

        Volley.newRequestQueue(this).add(stringRequest)

    }
}