package com.psm.proyecto_sm.utils

import android.app.AlertDialog
import android.content.Context

object DataManager {
    var userId: Long? = null
    var userProfilePic: ByteArray? = null

    var isConnected: Boolean = false;

    fun connectionAlert(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Sin Conexion a Internet")
        builder.setMessage("Sin internet no puede realizar la operación")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}