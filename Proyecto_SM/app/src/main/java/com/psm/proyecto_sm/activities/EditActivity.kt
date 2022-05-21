package com.psm.proyecto_sm.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.psm.proyecto_sm.R
import com.psm.proyecto_sm.utils.DatabaseHelper
import com.psm.proyecto_sm.utils.ImageController
import com.psm.proyecto_sm.models.User
import com.psm.proyecto_sm.utils.DataManager
import com.psm.proyecto_sm.utils.NetworkConnection
import kotlinx.android.synthetic.main.activity_edit.*
import org.json.JSONObject
import java.lang.Exception
import java.util.*

class EditActivity : AppCompatActivity() {

    val URL_READUSER = "http://cursoswelearn.xyz/phpApi/controllers/cReadUser.php?Id_User=" + DataManager.userId
    val URL_EDITUSER = "http://cursoswelearn.xyz/phpApi/controllers/cUpdateUser.php"

    val SELECT_ACTIVITY = 13
    var imageUri: Uri? = null
    var userAux = User()

    private lateinit var networkConnection: NetworkConnection

    private lateinit var progressDialog : ProgressDialog
    private lateinit var db : DatabaseHelper

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        iv_back_edit.setOnClickListener{goBacktoProfile()}
        iv_confirm_edit.setOnClickListener{
            if (DataManager.isConnected) {
                saveChanges()
            }
            else {
                DataManager.connectionAlert(this)
            }
        }
        iv_pfp_edit.setOnClickListener { selectImage() }

        db = DatabaseHelper(applicationContext)

        networkConnection = NetworkConnection(application)
        networkConnection.observe(this) { isConnected ->
            DataManager.isConnected = isConnected
        }

        getUser()
    }

    private fun goBacktoProfile() {
        val intent = Intent(this, ProfileActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(intent)
        finish()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveChanges() {
        userAux.name = et_username_edit.text.toString()
        userAux.email = et_email_edit.text.toString()
        userAux.password = et_password_edit.text.toString()
        userAux.phone = et_phone_edit.text.toString()
        userAux.address = et_direction_edit.text.toString()

        if (userAux.name!!.isEmpty()) { et_username_edit.setError("Nombre vacío") }
        else if (userAux.email!!.isEmpty()) { et_email_edit.setError("Email vacío") }
        else if (userAux.password!!.isEmpty()) { et_password_edit.setError("Contraseña vacía") }
        //else if (!db.validEmailUser(userAux.email, userAux.id_user!!)) { et_email_edit.setError("Email ya utilizado") }
        else {
            progressDialog = ProgressDialog(this)
            progressDialog.setMessage("Guardando cambios...")
            progressDialog.show()

            imageUri?.let {
                ImageController.saveImageUser(this, userAux, it)
            }

            userAux.update(this, URL_EDITUSER)
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getUser() {

        var stringRequest = StringRequest(Request.Method.GET, URL_READUSER, { response ->

            try {
                var jsonObject = JSONObject(response)
                userAux.id_user = jsonObject.getLong("id_user")
                userAux.name = jsonObject.getString("name")
                userAux.email = jsonObject.getString("email")
                userAux.password = jsonObject.getString("password")
                userAux.phone = jsonObject.getString("phone")
                userAux.address = jsonObject.getString("address")

                val imageStr = jsonObject.getString("profile_picture")
                if (imageStr.isNotEmpty()) {
                    val image = imageStr.replace("data:image/png;base64,", "")
                    userAux.profile_picture = Base64.getDecoder().decode(image)
                }

                userAux.register_date = jsonObject.getString("register_date")
            } catch (e : Exception) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
            } finally {
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

        }, { error ->
            Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
        })

        Volley.newRequestQueue(this).add(stringRequest)

    }
}