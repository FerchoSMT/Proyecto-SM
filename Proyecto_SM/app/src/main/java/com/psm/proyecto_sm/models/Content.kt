package com.psm.proyecto_sm.models

interface Content {
    fun create(db : DatabaseHelper) : Long
    fun update(db : DatabaseHelper)
    fun delete(db : DatabaseHelper)
}