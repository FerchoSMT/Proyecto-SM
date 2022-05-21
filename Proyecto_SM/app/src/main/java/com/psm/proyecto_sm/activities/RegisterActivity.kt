package com.psm.proyecto_sm.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.psm.proyecto_sm.utils.DatabaseHelper
import com.psm.proyecto_sm.utils.ImageController
import com.psm.proyecto_sm.models.User
import com.psm.proyecto_sm.R
import com.psm.proyecto_sm.utils.DataManager
import com.psm.proyecto_sm.utils.NetworkConnection
import kotlinx.android.synthetic.main.activity_register.*
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {

    val URL_CREATEUSER = "http://cursoswelearn.xyz/phpApi/controllers/cCreateUser.php"

    val SELECT_ACTIVITY = 13
    var imageUri: Uri? = null
    var userAux = User()

    private lateinit var networkConnection: NetworkConnection

    private lateinit var progressDialog : ProgressDialog
    private lateinit var db : DatabaseHelper

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        iv_image_register.setOnClickListener{selectImage()}
        btn_register_register.setOnClickListener{
            if (DataManager.isConnected) {
                signUp()
            }
            else {
                DataManager.connectionAlert(this)
            }
        }
        btn_login_register.setOnClickListener{gotoLogin()}

        db = DatabaseHelper(applicationContext)

        networkConnection = NetworkConnection(application)
        networkConnection.observe(this) { isConnected ->
            DataManager.isConnected = isConnected
        }
    }

    private fun gotoLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(intent)
        finish()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun signUp() {
        userAux.name = et_username_register.text.toString()
        userAux.email = et_email_register.text.toString()
        userAux.password = et_password_register.text.toString()
        userAux.phone = et_phone_register.text.toString()
        userAux.address = et_direction_register.text.toString()

        if (userAux.name!!.isEmpty()) { et_username_register.setError("Nombre vacío") }
        else if (userAux.email!!.isEmpty()) { et_email_register.setError("Email vacío") }
        else if (userAux.password!!.isEmpty()) { et_password_register.setError("Contraseña vacía") }
        else if(!isValidPassword(userAux.password!!)){ et_password_register.setError("Debe tener minimo 8 caracteres, una mayuscula, una minuscula y un número") }
        //else if (!db.validEmailUser(userAux.email, 0)) { et_email_register.setError("Email ya utilizado") }
        else {
            progressDialog = ProgressDialog(this)
            progressDialog.setMessage("Ingresando...")
            progressDialog.show()

            imageUri?.let {
                ImageController.saveImageUser(this, userAux, it)
            }

            userAux.signUp(this, URL_CREATEUSER)
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
        val passwordPattern = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}\$"
        val pattern = Pattern.compile(passwordPattern)
        val matcher = pattern.matcher(password)
        return matcher.matches()
    }
}