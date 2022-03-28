package com.psm.proyecto_sm.Models

data class Post (
    var id:String ="",
    var title:String="",
    var content: String="",
    var posted_date:String ="",
    var favorites:Int=0,
    var image1:String ="",
    var image2:String ="",
    var id_user:String =""
);

