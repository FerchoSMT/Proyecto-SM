package com.psm.proyecto_sm.activities

import android.app.ProgressDialog
import android.content.Intent
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
import com.psm.proyecto_sm.models.User
import com.psm.proyecto_sm.utils.DataManager
import com.psm.proyecto_sm.utils.NetworkConnection
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONArray
import java.util.*

class LoginActivity : AppCompatActivity() {

    val URL_READUSERS = "http://cursoswelearn.xyz/phpApi/controllers/cReadUsers.php"

    var userAux = User()
    var listUsers : MutableList<User> = mutableListOf()

    private lateinit var networkConnection: NetworkConnection

    private lateinit var progressDialog : ProgressDialog
    private lateinit var db : DatabaseHelper

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btn_login_login.setOnClickListener{loginUser()}
        btn_register_login.setOnClickListener{
            if (DataManager.isConnected) {
                gotoRegister()
            }
            else {
                DataManager.connectionAlert(this)
            }
        }

        db = DatabaseHelper(applicationContext)

        networkConnection = NetworkConnection(application)
        networkConnection.observe(this) { isConnected ->
            if (isConnected) {
                DataManager.isConnected = true
                getUsers()
            } else {
                DataManager.isConnected = false
            }
        }
    }

    private fun gotoRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(intent)
        finish()
    }

    private fun loginUser() {
        userAux.email = et_email_login.text.toString()
        userAux.password = et_password_login.text.toString()

        if (userAux.email!!.isEmpty()) { et_email_login.setError("Ingrese su email") }
        else if (userAux.password!!.isEmpty()) { et_password_login.setError("Ingrese su contraseña") }
        else {
            if (userAux.login(db)) {
                progressDialog = ProgressDialog(this)
                progressDialog.setMessage("Ingresando...")
                progressDialog.show()

                val intent = Intent(this, MainActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                startActivity(intent)
                finish()
            }
            else {
                Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show()
            }

        }
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
            }

        }, { error ->
            Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
        })

        Volley.newRequestQueue(this).add(stringRequest)

    }
}