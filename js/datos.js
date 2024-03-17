function obtenerUsuarios(){
    url = "http://168.194.207.98:8081/tp/lista.php?action=BUSCAR";

    fetch(url)
        .then((res) => {
            if (res.ok) {
                // Si la respuesta es exitosa, devolver el JSON
                return res.json();
            } else {
                throw new Error('Respuesta del servidor no exitosa');
            }
        })
        .then((jsonData) => {
            mostrarEnTabla(jsonData);
        })
        .catch((error) => console.error("Error:", error));
}

function mostrarEnTabla(jsonData) {
    let tbody = document.getElementById('tbody');
    let datos = '';
    console.log(jsonData)

    jsonData.forEach(element => {
        datos += `
        <tr>
            <td>${element.id}</td>
            <td>${element.usuario}</td>
            <td>${element.bloqueado}</td>
            <td>${element.apellido}</td>
            <td>${element.nombre}</td>
        </tr>
        `    
        tbody.innerHTML = datos;

    });
}

obtenerUsuarios();
