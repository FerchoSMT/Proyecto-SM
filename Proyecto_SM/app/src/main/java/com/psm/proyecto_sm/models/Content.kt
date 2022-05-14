package com.psm.proyecto_sm.models

import com.psm.proyecto_sm.Utils.DatabaseHelper

interface Content {
    fun create(db : DatabaseHelper) : Long
    fun update(db : DatabaseHelper)
    fun delete(db : DatabaseHelper)
}