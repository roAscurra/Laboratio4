<?php

$servername = "localhost";
$username = "root";
$password = "";

// Create connection
$conn = new mysqli($servername, $username, $password);

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// Nombre de la base de datos
$database = "db";

// Crear la base de datos si no existe
$sql_create_db = "CREATE DATABASE IF NOT EXISTS $database";
if ($conn->query($sql_create_db) === TRUE) {
    echo "Base de datos '$database' creada correctamente.<br>";
} else {
    echo "Error al crear la base de datos '$database': " . $conn->error . "<br>";
}

// Cerrar la conexión temporal a la base de datos
$conn->close();

// Establecer conexión a la base de datos creada
$conn = new mysqli($servername, $username, $password, $database);

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// Función para crear la tabla Pais si no existe
function crearTablaPais($conn) {
    $sql = "CREATE TABLE IF NOT EXISTS Pais (
        codigoPais INT PRIMARY KEY,
        nombrePais VARCHAR(50) NOT NULL,
        capitalPais VARCHAR(50) NOT NULL,
        region VARCHAR(50) NOT NULL,
        poblacion BIGINT NOT NULL,
        latitud DECIMAL(10, 8) NOT NULL,
        longitud DECIMAL(11, 8) NOT NULL
    )";

    if ($conn->query($sql) === TRUE) {
        echo "Tabla Pais creada correctamente.<br>";
    } else {
        echo "Error al crear la tabla Pais: " . $conn->error . "<br>";
    }
}

// Llamar a la función para crear la tabla Pais
crearTablaPais($conn);

$conn->set_charset("utf8");

?>
