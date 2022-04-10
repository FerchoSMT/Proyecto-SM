package com.psm.proyecto_sm.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.psm.proyecto_sm.R
import kotlinx.android.synthetic.main.activity_draft.*

class DraftActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_draft)

        iv_back_draft.setOnClickListener{gobacktoMain()}
    }

    private fun gobacktoMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(intent)
        finish()
    }

    // TODO: Adaptador para hacer click en cualquier draft y abrir PublishActivity con la info del Draft
}