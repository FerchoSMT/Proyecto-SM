<?php
include("config.php");

$content = $_POST['content'];
$id_user = $_POST['id_user'];
$id_post = $_POST['id_post'];

$sql = 'INSERT INTO REPLIES (?,?,?,?,?,?);';

$statement = $this->con->prepare($sql);
$statement->bindParam(1,$content);
$statement->bindParam(2,$id_user);
$statement->bindParam(3,$id_post);

$statement->execute();
?>