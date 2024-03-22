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
    let clase = '';
    jsonData.forEach(element => {
        if(element.bloqueado == 'Y'){
            clase = 'bloqueado';
        }else if(element.bloqueado == 'N'){
            clase = 'desbloqueado';
        }
        datos += `
        <tr class="${clase}">
            <td>${element.id}</td>
            <td>${element.usuario}</td>
            <td>${element.bloqueado}</td>
            <td>${element.apellido}</td>
            <td>${element.nombre}</td>
            <td><button class="${clase}" id="bloquear" data-id="${element.id}"></button></td>
            <td><button class="${clase}" id="desbloquear" data-id="${element.id}"></button></td>
        </tr>
        `    
        tbody.innerHTML = datos;

    });
}

function bloquearUser(idUser){
    let url = 'http://168.194.207.98:8081/tp/lista.php?action=BLOQUEAR&idUser='+ idUser +'&estado=Y';
    fetch(url)
    .then((res) => res.json())
    .then((response) => {
        // console.log("Success:", response);
        window.location.reload();
    })
    .catch((error) => console.error("Error:", error));  
}
function desbloquearUser(idUser){
    let url = 'http://168.194.207.98:8081/tp/lista.php?action=BLOQUEAR&idUser='+ idUser +'&estado=N';
    fetch(url)
    .then((res) => res.json())
    .then((response) => {
        // console.log("Success:", response);
        window.location.reload();
    })
    .catch((error) => console.error("Error:", error));
}

document.addEventListener('click', function(event) {
    if (event.target.id === 'desbloquear') {
        if (event.target.classList.contains('desbloqueado')) {
            alert("El usuario ya se encuentra desbloqueado.")
        }else{
            const id = event.target.getAttribute('data-id');
            desbloquearUser(id);
        }
    }
});

document.addEventListener('click', function(event) {
    if (event.target.id === 'bloquear') {
        if (event.target.classList.contains('bloqueado')) {
            alert("El usuario ya se encuentra bloqueado.");
        }else{
            const id = event.target.getAttribute('data-id');
            bloquearUser(id);
        }

    }
});

obtenerUsuarios();
