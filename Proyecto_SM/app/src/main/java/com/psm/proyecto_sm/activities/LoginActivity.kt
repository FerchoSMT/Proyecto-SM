package com.psm.proyecto_sm.activities

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.psm.proyecto_sm.R
import com.psm.proyecto_sm.models.DatabaseHelper
import com.psm.proyecto_sm.models.User
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    var userAux = User()

    private lateinit var progressDialog : ProgressDialog
    private lateinit var db : DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btn_login_login.setOnClickListener{loginUser()}
        btn_register_login.setOnClickListener{gotoRegister()}

        db = DatabaseHelper(applicationContext)
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
}