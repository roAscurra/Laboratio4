# Trabajo Práctico N° 1 - Laboratorio de Computación 4

Este repositorio contiene el código para el Trabajo Práctico N° 1 del Laboratorio de Computación 4.

## Descripción

El objetivo de este trabajo práctico es desarrollar una aplicación web utilizando únicamente HTML y JavaScript (sin el uso de librerías o frameworks) que cumpla con los siguientes requisitos:

1. Implementar un formulario HTML llamado `login.html` que permita el ingreso de un usuario y contraseña.
2. Al hacer clic en el botón "Ingresar", llamar a una función JavaScript que realice una solicitud a la siguiente URL: `http://168.194.207.98:8081/tp/login.php`, pasando los datos del formulario. La respuesta esperada es un JSON con campos "respuesta" y "mje".
3. En caso de respuesta "ERROR", mostrar el mensaje al usuario. En caso de respuesta "OK", redirigir la aplicación a una nueva página HTML llamada `lista.html`.
4. En la página `lista.html`, mostrar una tabla que liste los datos obtenidos de la URL `http://168.194.207.98:8081/tp/lista.php?action=BUSCAR`. Incluir una caja de texto y un botón para buscar usuarios.
5. Los resultados de la búsqueda deben cargarse en la tabla.
6. Agregar dos nuevas columnas a la derecha de la tabla para marcar usuarios como BLOQUEADO o DESBLOQUEADO.
7. Incluir botones en las nuevas columnas para bloquear y desbloquear usuarios. Las llamadas correspondientes deben realizarse a través de las URL proporcionadas:
- **La llamada para bloquear será la siguiente:**
`http://168.194.207.98:8081/tp/lista.php?action=BLOQUEAR&idUser=XXXX&estado=Y`
- **Y la llamada para desbloquear**
`http://168.194.207.98:8081/tp/lista.php?action=BLOQUEAR&idUser=XXXX&estado=N`
.
8. Aplicar estilos CSS para que las filas de la tabla que contengan usuarios bloqueados tengan un fondo de color #fd9f8b, y las filas de usuarios no bloqueados tengan un fondo de color #cef8c6.

## Alumna: 
- **Nombre:** Romina Ascurra

