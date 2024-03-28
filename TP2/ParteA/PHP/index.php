<?php
include "cnx/cnx.php";

// Función para hacer solicitudes en paralelo y evitar retardo en la ejecución
function solicitudes($urls) {
    $multiHandler = curl_multi_init();
    $curlHandlers = [];

    // Crear las solicitudes cURL individuales
    foreach ($urls as $url) {
        $curlHandlers[] = controllerCurl($url, $multiHandler);
    }

    // Ejecutar las solicitudes en paralelo
    $running = null;
    do {
        curl_multi_exec($multiHandler, $running);
    } while ($running);

    // Recopilar los resultados de las solicitudes
    $results = [];
    foreach ($curlHandlers as $handler) {
        $results[] = curl_multi_getcontent($handler);
        curl_multi_remove_handle($multiHandler, $handler);
    }

    // Cerrar el manejador múltiple
    curl_multi_close($multiHandler);

    return $results;
}

// Función para crear un controlador cURL individual
function controllerCurl($url, $multiHandler) {
    $ch = curl_init($url);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_multi_add_handle($multiHandler, $ch);
    return $ch;
}

// Array para almacenar las URLs de las solicitudes a la API
$urls = [];
for ($callingCode = 1; $callingCode <= 300; $callingCode++) {
    $urls[] = "https://restcountries.com/v2/callingcode/{$callingCode}";
}

// Obtener los resultados de las solicitudes en paralelo
$responses = solicitudes($urls);

// Procesar las respuestas
foreach ($responses as $index => $response) {
    $data = json_decode($response, true);
    // Verificar si la respuesta contiene el mensaje "Not Found"
    if (strpos($response, "Not Found") !== false) {
        // Si se encuentra el mensaje "Not Found", obtener el callingCode a partir del URL
        $callingCode = $index + 1; // El índice más 1 representa el callingCode
        //echo "Error 404 en el callingCode: ".$callingCode."<br>";
    }
    if (!empty($data)) {
        // echo json_encode($data, JSON_PRETTY_PRINT);
        //verifico si todos los datos están presentes para poder insertarlos
        if(isset($data[0]['callingCodes']) && isset($data[0]['name']) && isset($data[0]['capital']) && isset($data[0]['region']) && isset($data[0]['population']) && isset($data[0]['latlng'][0]) && isset($data[0]['latlng'][1])){
            $codigoPais = $data[0]['callingCodes'];
            $nombrePais = $data[0]['name'];
            $capitalPais = $data[0]['capital'];
            $region = $data[0]['region'];
            $poblacion = $data[0]['population'];
            $latitud = $data[0]['latlng'][0];
            $longitud = $data[0]['latlng'][1];

            $nombrePais = mysqli_real_escape_string($conn, $nombrePais);
            $capitalPais = mysqli_real_escape_string($conn, $capitalPais);
            $region = mysqli_real_escape_string($conn, $region);
            
            echo $codigoPais = $codigoPais[0]; 
            echo "<br>"; 
            
            $sql = "SELECT * FROM pais WHERE codigoPais = '$codigoPais'";
            $query = mysqli_query($conn, $sql);
            $numrows = mysqli_num_rows($query);
            if($numrows > 0){
                $sql2 = "UPDATE pais SET nombrePais = '$nombrePais', capitalPais = '$capitalPais', region = '$region', poblacion = '$poblacion', latitud = '$latitud', longitud = '$longitud'
                WHERE codigoPais = '$codigoPais'";
                $query2 = mysqli_query($conn, $sql2);
                if($query2){
                    echo "Se actualizó el registro con códigoPais igual a ".$codigoPais;
                    echo "<br>"; 
                }
            }else{
                // Insertar los datos en la tabla Pais
                $sql3 = "INSERT INTO pais (codigoPais, nombrePais, capitalPais, region, poblacion, latitud, longitud) 
                VALUES ('$codigoPais', '$nombrePais', '$capitalPais', '$region', $poblacion, $latitud, $longitud)";

                if (mysqli_query($conn, $sql3) === TRUE) {
                    echo "Datos de $codigoPais insertados correctamente.<br>";
                } else {
                    echo "Error al insertar datos de $nombrePais: " . $conn->error . "<br>";
                }
            }

        }
    } 
}

mysqli_close($conn);

?>



