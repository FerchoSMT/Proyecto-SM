<?php
require_once $_SERVER['DOCUMENT_ROOT'] . './phpApi/model/config.php';


$title = $_POST['title'];
$content = $_POST['content'];
$id_user = $_POST['id_user'];
//Cambiar en sentencia para update
$sql = 'INSERT INTO POSTS (?,?,?,?,?,?);';

$statement = $this->con->prepare($sql);
$statement->bindParam(1,$title);
$statement->bindParam(2,$content);
$statement->bindParam(3,$id_user);

//meter lo de las tablas de imagenes

$statement->execute();


?>