function login(){
    document.getElementById('formLogin').addEventListener('submit', function(event) {
        event.preventDefault();

        //recupero valores de los inputs
        let user = document.getElementById('user').value;
        let pass = document.getElementById('pass').value;
    

        let url = "http://168.194.207.98:8081/tp/login.php?user="+user+"&&pass="+pass;
        fetch(url)
            .then((res) => res.json())
            .then((response) => {
                // console.log("Success:", response);
                respuestaLogin(response);
            })
            .catch((error) => console.error("Error:", error));  
            });
}
function respuestaLogin(respuesta){
    let msjerror = document.getElementById('error');
    switch(respuesta.respuesta){
        case "OK": 
            window.location.href = "vistas/lista.html";
            break;
        case "ERROR":
            msjerror.style.display = 'block';
            break;
    }
}
login()