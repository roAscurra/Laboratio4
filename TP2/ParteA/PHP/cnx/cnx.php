<?php

$servername = "localhost";

$username = "root";

$password = "";

$database = "test";

// Create connection

$conn = new mysqli($servername, $username, $password, $database);

// Check connection

if ($conn->connect_error) {

  die("Connection failed: " . $conn->connect_error);

}

$conn->set_charset("utf8");

?>