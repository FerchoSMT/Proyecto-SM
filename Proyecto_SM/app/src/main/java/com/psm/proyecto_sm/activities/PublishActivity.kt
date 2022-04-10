package com.psm.proyecto_sm.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.psm.proyecto_sm.Models.Post
import com.psm.proyecto_sm.R
import kotlinx.android.synthetic.main.activity_publish.*

class PublishActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_publish)
        iv_back_publish.setOnClickListener{gotoMain()}
        iv_confirm_publish.setOnClickListener{postPost()}
        iv_image_publish.setOnClickListener{selectImage()}
    }

    private fun selectImage() {
        TODO("Not yet implemented")
    }

    private fun postPost() {
        val intent = Intent(this, PostActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(intent)
        finish()
    }

    private fun gotoMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(intent)
        finish()
    }
}