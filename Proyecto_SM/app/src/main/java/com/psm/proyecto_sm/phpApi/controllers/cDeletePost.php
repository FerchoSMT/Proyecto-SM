<?php
include("config.php");

$id_post = $_POST['id_post'];

//ver como pasar profilePic de la manera correcta;
$sql = 'DELETE FROM POSTS WHERE id_post = ?;';
$sql2 ='DELETE FROM FAVS WHERE id_post = ?;';
$sql3 ='DELETE FROM REPLIES WHERE id_post = ?;';
$sql4 ='DELETE FROM POST_IMAGES WHERE id_post = ?;';

$statement = $this->con->prepare($sql);
$statement->bindParam(1,$id_post);
$statement2 = $this->con->prepare($sql2);
$statement2->bindParam(1,$id_post);
$statement3 = $this->con->prepare($sql3);
$statement3->bindParam(1,$id_post);
$statement4 = $this->con->prepare($sql4);
$statement4->bindParam(1,$id_post);

$statement->execute();
$statement2->execute();
$statement3->execute();
$statement4->execute();
?>