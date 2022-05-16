<?php

class UserModel {
    public $id_user;
    public $name;
    public $profilePic;
    public $email;
    public $password;
    public $address;
    public $phone;

    public function addUserID($id_user){
        $this->id_user = $id_user;
    }

    public function addUser($id_user, $name, $profilePic, $email, $password, $phone){
        $this->id_user = $id_user;
        $this->name = $name;
        $this->profilePic = $NomprofilePicbre;
        $this->email = $email;
        $this->password = $password;
        $this->phone = $phone;
    }

}