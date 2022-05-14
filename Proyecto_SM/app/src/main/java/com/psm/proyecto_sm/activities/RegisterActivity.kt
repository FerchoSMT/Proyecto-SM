package com.psm.proyecto_sm.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.psm.proyecto_sm.Utils.DatabaseHelper
import com.psm.proyecto_sm.Utils.ImageController
import com.psm.proyecto_sm.models.User
import com.psm.proyecto_sm.R
import kotlinx.android.synthetic.main.activity_register.*
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {

    val SELECT_ACTIVITY = 13
    var imageUri: Uri? = null
    var userAux = User()

    private lateinit var progressDialog : ProgressDialog
    private lateinit var db : DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        iv_image_register.setOnClickListener{selectImage()}
        btn_register_register.setOnClickListener{signUp()}
        btn_login_register.setOnClickListener{gotoLogin()}

        db = DatabaseHelper(applicationContext)
    }

    private fun gotoLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(intent)
        finish()
    }

    private fun signUp() {
        userAux.name = et_username_register.text.toString()
        userAux.email = et_email_register.text.toString()
        userAux.password = et_password_register.text.toString()
        userAux.phone = et_phone_register.text.toString()
        userAux.address = et_direction_register.text.toString()

        if (userAux.name!!.isEmpty()) { et_username_register.setError("Nombre vacío") }
        else if (userAux.email!!.isEmpty()) { et_email_register.setError("Email vacío") }
        else if (userAux.password!!.isEmpty()) { et_password_register.setError("Contraseña vacía") }
        else if(!isValidPassword(userAux.password!!)){ et_password_register.setError("Usar una mayuscula, un numero y un caracter especial") }
        else if (!db.validEmailUser(userAux.email, 0)) { et_email_register.setError("Email ya utilizado") }
        else {
            progressDialog = ProgressDialog(this)
            progressDialog.setMessage("Ingresando...")
            progressDialog.show()

            imageUri?.let {
                ImageController.saveImageUser(this, userAux, it)
            }

            userAux.signUp(db)

            val intent = Intent(this, MainActivity::class.java)
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

                iv_image_register.setImageURI(imageUri)
            }
        }
    }

    fun isValidPassword(password : String): Boolean {
        val passwordPattern = "^(?=.[0-9])(?=.[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$"
        val pattern = Pattern.compile(passwordPattern)
        val matcher = pattern.matcher(password)
        return matcher.matches()
    }
}