package com.psm.proyecto_sm.activities

import android.app.ProgressDialog
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.psm.proyecto_sm.R
import com.psm.proyecto_sm.Utils.DatabaseHelper
import com.psm.proyecto_sm.Utils.NetworkChangeListener
import com.psm.proyecto_sm.models.User
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    var userAux = User()

    var networkChangeListener = NetworkChangeListener()

    private lateinit var progressDialog : ProgressDialog
    private lateinit var db : DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        /*val crashButton = Button(this)
        crashButton.text = "Test Crash"
        crashButton.setOnClickListener {
            throw RuntimeException("Test Crash") // Force a crash
        }

        addContentView(crashButton, ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT))*/

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

    override fun onStart() {
        var filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkChangeListener, filter)

        super.onStart()
    }

    override fun onStop() {
        unregisterReceiver(networkChangeListener)

        super.onStop()
    }
}