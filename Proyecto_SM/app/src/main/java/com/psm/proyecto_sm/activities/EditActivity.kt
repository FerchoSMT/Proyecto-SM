package com.psm.proyecto_sm.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.psm.proyecto_sm.R
import com.psm.proyecto_sm.models.DatabaseHelper
import com.psm.proyecto_sm.models.ImageController
import com.psm.proyecto_sm.models.User
import com.psm.proyecto_sm.models.UserLogged
import kotlinx.android.synthetic.main.activity_edit.*

class EditActivity : AppCompatActivity() {

    val SELECT_ACTIVITY = 13
    var imageUri: Uri? = null
    var userAux = User()

    private lateinit var progressDialog : ProgressDialog
    private lateinit var db : DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        iv_back_edit.setOnClickListener{goBacktoProfile()}
        iv_confirm_edit.setOnClickListener{saveChanges()}
        iv_pfp_edit.setOnClickListener { selectImage() }

        db = DatabaseHelper(applicationContext)

        if (UserLogged.userId != null) {
            userAux = db.readUser(UserLogged.userId!!)
            et_username_edit.setText(userAux.name)
            et_email_edit.setText(userAux.email)
            et_password_edit.setText(userAux.password)
            et_phone_edit.setText(userAux.phone)
            et_direction_edit.setText(userAux.address)
            val profilePicBmp = ImageController.getImageBitmap(userAux.profile_picture)
            if (profilePicBmp != null) {
                iv_pfp_edit.setImageBitmap(profilePicBmp)
            }
        }
    }

    private fun goBacktoProfile() {
        val intent = Intent(this, ProfileActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(intent)
        finish()
    }

    private fun saveChanges() {
        userAux.name = et_username_edit.text.toString()
        userAux.email = et_email_edit.text.toString()
        userAux.password = et_password_edit.text.toString()
        userAux.phone = et_phone_edit.text.toString()
        userAux.address = et_direction_edit.text.toString()

        if (userAux.name!!.isEmpty()) { et_username_edit.setError("Nombre vacío") }
        else if (userAux.email!!.isEmpty()) { et_email_edit.setError("Email vacío") }
        else if (userAux.password!!.isEmpty()) { et_password_edit.setError("Contraseña vacía") }
        else if (!db.validEmailUser(userAux.email, userAux.id_user!!)) { et_email_edit.setError("Email ya utilizado") }
        else {
            progressDialog = ProgressDialog(this)
            progressDialog.setMessage("Guardando cambios...")
            progressDialog.show()

            imageUri?.let {
                ImageController.saveImageUser(this, userAux, it)
            }

            userAux.update(db)

            val intent = Intent(this, ProfileActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            startActivity(intent)
            finish()
        }
    }

    private fun selectImage() {
        ImageController.selectPhotoFromGallery(this, SELECT_ACTIVITY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when {
            requestCode == SELECT_ACTIVITY && resultCode == Activity.RESULT_OK -> {
                imageUri = data!!.data

                iv_pfp_edit.setImageURI(imageUri)
            }
        }
    }
}