<?php
include("config.php");

$id_user =$_POST['id_user']
$name = $_POST['name'];
$email = $_POST['email'];
$password = $_POST['password'];
$phone = $_POST['phone'];
$address = $_POST['address'];
$profilePic = file_get_contents(addslashes($_FILES["profilePic"]["tmp_name"]));

//ver como pasar profilePic de la manera correcta;
$sql = 'UPDATE POSTS SET name=?,email=?,password=?,password=?,phone=?,address=?,profilePic=? WHERE id_user=? ';

$statement = $this->con->prepare($sql);
$statement->bindParam(1,$name);
$statement->bindParam(2,$email);
$statement->bindParam(3,$password);
$statement->bindParam(4,$phone);
$statement->bindParam(5,$address);
$statement->bindParam(6,$profilePic);
$statement->bindParam(7,$id_user);
$statement->execute();

?>