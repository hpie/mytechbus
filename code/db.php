<?php
/*
$servername = "localhost";
$username	= "s7hpiein_techbus";
$password	= "myt3chbu$";
$dbname		= "s7hpiein_mytechbus";
*/

$servername = "localhost";
$username	= "root";
$password	= "";
$dbname		= "mytechbus2";

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 
?>