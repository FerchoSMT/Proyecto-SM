<?php

define('DB_HOST','localhost');
define('DB_USERNAME','root');
define('DB_PASSWORD','');
define('DB_NAME', 'bd_buzztalk');
 
//Connecting to the database
$conn = new mysqli(DB_HOST, DB_USERNAME, DB_PASSWORD, DB_NAME);
 
//checking the successful connection
if($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
 
//making an array to store the response
$response = array(); 
 
//if there is a post request move ahead 
if($_SERVER['REQUEST_METHOD'] == 'POST') {
 
    //getting the values from request 
    $name = $_POST['name']; 
    $email = $_POST['email'];
    $password = $_POST['password'];
    $phone = $_POST['phone'];
    $address = $_POST['address'];
    $profile_pic = $_POST['profile_picture'];
 
    //creating a statement to insert to database 
    $stmt = $conn->prepare("INSERT INTO user (Name, Email, Password, Phone, Address, Profile_Picture) VALUES (?, ?, ?, ?, ?, ?)");
    //binding the parameter to statement 
    $stmt->bind_param("sssisb", $name, $email, $password, $phone, $address, $profile_pic);
    
    //if data inserts successfully
    if($stmt->execute()) {
        //making success response 
        $response['error'] = false; 
        $response['message'] = 'Name saved successfully'; 
    } else {
        //if not making failure response 
        $response['error'] = true; 
        $response['message'] = 'Please try later';
    }
 
} else {
    $response['error'] = true; 
    $response['message'] = "Invalid request"; 
}
 
//displaying the data in json format 
echo json_encode($response);