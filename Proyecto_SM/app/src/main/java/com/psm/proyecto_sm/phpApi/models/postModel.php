<?php

class PostModel {
    public $id_post;
    public $id_user;
    public $title;
    public $content;
    public $favorites;
    public $isDraft;
    public $posted_date;
    public $image1;
    public $image2;

    public function addPostId($id_user){
        $this->id_user = $id_user;
    }

    public function addPost($id_post, $id_user, $title, $content, $favorites, $isDraft,$posted_date,$image1,$image2){
        $this->id_post = $id_post;
        $this->id_user = $id_user;
        $this->title = $title;
        $this->content = $content;
        $this->favorites = $favorites;
        $this->isDraft = $isDraft;
        $this->posted_date = $posted_date;
        $this->isDraft = $isDraft;
        $this->image1 = $image1;
        $this->image2 = $image2;
    }
}