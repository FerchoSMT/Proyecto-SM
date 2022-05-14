package com.psm.proyecto_sm.activities

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.psm.proyecto_sm.R
import com.psm.proyecto_sm.Utils.DatabaseHelper
import com.psm.proyecto_sm.Utils.ImageController
import com.psm.proyecto_sm.models.Post
import com.psm.proyecto_sm.Utils.UserLogged
import kotlinx.android.synthetic.main.activity_publish.*

class PublishActivity : AppCompatActivity() {

    val SELECT_ACTIVITY = 13
    var imageNum = 0
    var imageUri1: Uri? = null
    var imageUri2: Uri? = null

    var postAux = Post()
    var idPost : Long? = null
    var isDraft : Boolean? = null

    private lateinit var progressDialog: ProgressDialog
    private lateinit var builder : AlertDialog.Builder
    private lateinit var db : DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_publish)

        txt_drafts.setOnClickListener { gotoDrafts() }
        iv_back_publish.setOnClickListener{gotoMain()}
        iv_confirm_publish.setOnClickListener{postPost()}
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

        if (UserLogged.userId != null) {
            val profilePicBmp = ImageController.getImageBitmap(UserLogged.userProfilePic)
            if (profilePicBmp != null) {
                iv_pfpic_publish.setImageBitmap(profilePicBmp)
            }
        }

        if (idPost!!.toInt() != 0) { infoPost() }
    }

    private fun infoPost() {
        postAux = db.readPost(idPost!!)

        txt_drafts.visibility = View.GONE
        et_title_publish.setText(postAux.title)
        et_content_post.setText(postAux.content)
        if (postAux.images.size == 1) {
            iv_image_publish.setImageBitmap(ImageController.getImageBitmap(postAux.images[0]))
            iv_image_publish2.visibility = View.VISIBLE
        }
        else if (postAux.images.size == 2) {
            iv_image_publish.setImageBitmap(ImageController.getImageBitmap(postAux.images[0]))
            iv_image_publish2.visibility = View.VISIBLE
            iv_image_publish2.setImageBitmap(ImageController.getImageBitmap(postAux.images[1]))
        }
    }

    private fun postPost() {
        postAux.title = et_title_publish.text.toString()
        postAux.content = et_content_post.text.toString()
        postAux.id_user = UserLogged.userId

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

            if (isDraft == true) { postAux.updateDraft(db) }
            else {
                if (idPost!!.toInt() == 0) { idPost = postAux.create(db) }
                else { postAux.update(db) }
            }

            val intent = Intent(this, PostActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            intent.putExtra("IdPost", idPost)
            startActivity(intent)
            finish()
        }
    }

    private fun gotoMain() {
        if (idPost!!.toInt() == 0) {
            builder = AlertDialog.Builder(this)
            builder.setMessage("¿Guardar borrador?")
            builder.setPositiveButton("Guardar") { dialogInterface, which ->
                postAux.title = et_title_publish.text.toString()
                postAux.content = et_content_post.text.toString()
                postAux.id_user = UserLogged.userId

                if (postAux.title!!.isEmpty()) { et_title_publish.setError("Título vacío") }
                else if (postAux.content!!.isEmpty()) { et_content_post.setError("Contenido vacío") }
                else {
                    imageUri1?.let {
                        ImageController.savePostImage1(this, postAux, it)
                    }
                    imageUri2?.let {
                        ImageController.savePostImage2(this, postAux, it)
                    }

                    postAux.saveDraft(db)

                    val intent = Intent(this, MainActivity::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                    startActivity(intent)
                    finish()
                }
            }
            builder.setNegativeButton("Cancelar") { dialogInterface, which -> }
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
}