<?php
require_once $_SERVER['DOCUMENT_ROOT'] . './phpApi/model/config.php';

$id_reply = $_POST['id_reply'];

//ver como pasar profilePic de la manera correcta;
$sql = 'DELETE FROM REPLIES WHERE id_reply = ?;';

$statement = $this->con->prepare($sql);
$statement->bindParam(1,$id_reply);

$statement->execute();
?>