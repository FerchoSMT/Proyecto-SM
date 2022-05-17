<?php
require_once $_SERVER['DOCUMENT_ROOT'] . './phpApi/model/config.php';


$title = $_POST['title'];
$content = $_POST['content'];
$id_user = $_POST['id_user'];
if( $_POST['image1']!=null){
    $image1 = $_POST['image1'];
}

if( $_POST['image2'];){
    $image2 = $_POST['image2'];
}


$sql = 'INSERT INTO POSTS (title,content,id_user) VALUES (?,?,?);';

$statement = $this->con->prepare($sql);
$statement->bindParam(1,$title);
$statement->bindParam(2,$content);
$statement->bindParam(3,$id_user);

//meter lo de las tablas de imagenes

$statement->execute();


?>