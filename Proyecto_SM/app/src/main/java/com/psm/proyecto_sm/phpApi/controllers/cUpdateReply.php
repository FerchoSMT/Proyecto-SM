<?php
require_once $_SERVER['DOCUMENT_ROOT'] . './phpApi/model/config.php';

$id_reply = $_POST['id_reply'];
$content = $_POST['content'];
$id_user = $_POST['id_user'];
$id_post = $_POST['id_post'];


//ver como pasar profilePic de la manera correcta;
$sql = 'UPDATE REPLIES SET content=?,id_user=?,id_post=? WHERE id_reply=? ';

$statement = $this->con->prepare($sql);
$statement->bindParam(1,$id_reply);
$statement->bindParam(2,$content);
$statement->bindParam(3,$id_user);
$statement->bindParam(4,$id_post);
$statement->execute();

?>