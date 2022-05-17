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
        $this->id_reply = $id_reply;
        $this->content = $content;
        $this->replied_date = $replied_date;
        $this->name_user = $name_user;
        $this->img_user = $img_user;
    }
}