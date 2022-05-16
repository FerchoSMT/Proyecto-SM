<?php

class ReplyModel {
    public $id_post;
    public $id_user;
    public $id_reply;
    public $content;
    public $replied_date;
    public $name_user;
    public $img_user;

    public function addReplyId($id_user){
        $this->id_user = $id_user;
    }

    public function addReply($id_post, $id_user, $id_reply, $content, $replied_date, $name_user,$img_user){
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