package com.psm.proyecto_sm.models

import android.content.Context

interface Content {
    fun create(context: Context, url: String)
    fun update(context: Context, url: String)
    fun delete(context: Context, url: String)
}