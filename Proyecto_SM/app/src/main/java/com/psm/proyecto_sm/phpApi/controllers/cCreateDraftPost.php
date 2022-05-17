<?php
require_once $_SERVER['DOCUMENT_ROOT'] . './phpApi/model/config.php';


$title = $_POST['title'];
$content = $_POST['content'];
$id_user = $_POST['id_user'];
$image1 = null;
$image2 = null;
if( $_POST['image1']!=null){
    //$image1 = $_POST['image1'];
    $image1 = file_get_contents(addslashes($_FILES["image1"]["tmp_name"]));
}

if( $_POST['image2'];){
    //$image2 = $_POST['image2'];
    $image2 = file_get_contents(addslashes($_FILES["image2"]["tmp_name"]));
}

$sql = 'INSERT INTO POSTS (title,content,id_user) VALUES (?,?,?);';

$statement = $this->con->prepare($sql);
$statement->bindParam(1,$title);
$statement->bindParam(2,$content);
$statement->bindParam(3,$id_user);



//meter lo de las tablas de imagenes

$statement->execute();


?>