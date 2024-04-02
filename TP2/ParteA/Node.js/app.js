require('dotenv').config();

const mysql = require('mysql');

const axios = require("axios");

// Crear una conexión a la base de datos utilizando las variables de entorno
const connection = mysql.createConnection({
  host: process.env.DB_HOST,
  user: process.env.DB_USER,
  password: process.env.DB_PASSWORD
});
// Conectar a la base de datos
connection.connect((err) => {
  if (err) {
    console.error('Error al conectar a la base de datos:', err);
    return;
  }

  console.log('Conexión exitosa');

  // Crear la base de datos si no existe
  connection.query(`CREATE DATABASE IF NOT EXISTS ${process.env.DB_NAME}`, (err) => {
    if (err) {
      console.error('Error al crear la base de datos:', err);
      connection.end(); // Cerrar la conexión en caso de error
      return;
    }

    console.log(`Base de datos "${process.env.DB_NAME}" creada correctamente o ya existe`);

    // Seleccionar la base de datos
    connection.query(`USE ${process.env.DB_NAME}`, (err) => {
      if (err) {
        console.error('Error al seleccionar la base de datos:', err);
        connection.end(); // Cerrar la conexión en caso de error
        return;
      }

      console.log(`Base de datos "${process.env.DB_NAME}" seleccionada`);

      // Crear la tabla si no existe
      const createTableQuery = `
        CREATE TABLE IF NOT EXISTS Pais (
          codigoPais INT PRIMARY KEY,
          nombrePais VARCHAR(50) NOT NULL,
          capitalPais VARCHAR(50) NOT NULL,
          region VARCHAR(50) NOT NULL,
          poblacion BIGINT NOT NULL,
          latitud DECIMAL(10, 8) NOT NULL,
          longitud DECIMAL(11, 8) NOT NULL
        )
      `;
      
      connection.query(createTableQuery, (err) => {
        if (err) {
          console.error('Error al crear la tabla:', err);
          connection.end(); // Cerrar la conexión en caso de error
          return;
        }

        console.log('Tabla "Pais" creada correctamente o ya existe');

        // Realizar consultas para obtener los datos de los países
        getPaises();
      });
    });
  });
});


  const getPaises = async () => {
    const promesas = [];
    const inserciones = [];
    const sinDatos = [];
    const updates = [];
    let consultasCompletas = 0;
    const totalConsultas = 300; // Total de consultas esperadas
  
    for (let i = 1; i <= 300; i++) {
      promesas.push(
        axios.get(`https://restcountries.com/v2/callingcode/${i}`)
          .then(response => {
            const data = response.data;
            if (data[0].callingCodes[0] && data[0].name && data[0].region && data[0].population && data[0].latlng[0] && data[0].latlng[1]) {
              const codigoPais = response.data[0].callingCodes[0];
              const nombrePais = response.data[0].name;
              const capitalPais = response.data[0].capital;
              const region = response.data[0].region;
              const poblacion = response.data[0].population;
              const latitud = response.data[0].latlng[0];
              const longitud = response.data[0].latlng[1];
              connection.query(`SELECT * FROM Pais WHERE codigoPais = '${codigoPais}'`, (error, results, fields) => {
                if (error) {
                  console.error('Error al ejecutar la consulta:', error);
                  return;
                }
                if(results.length == 0){
                  inserciones.push(codigoPais);
                  connection.query(
                    `INSERT INTO Pais (codigoPais, nombrePais, capitalPais, region, poblacion, latitud, longitud) VALUES (?, ?, ?, ?, ?, ?, ?)`,
                    [codigoPais, nombrePais, capitalPais, region, poblacion, latitud, longitud],
                    (error, results, fields) => {
                      if (error) {
                        console.error('Error al insertar el país:', error);
                        return;
                      }
                      // console.log(`País ${nombrePais} insertado correctamente.`);
                      consultasCompletas++;
                    }
                  );
                  
                }else{
                  updates.push(codigoPais)
                    // Si el país ya existe, actualizar sus datos
                    connection.query(
                      `UPDATE Pais SET nombrePais = ?, capitalPais = ?, region = ?, poblacion = ?, latitud = ?, longitud = ? WHERE codigoPais = ?`,
                      [nombrePais, capitalPais, region, poblacion, latitud, longitud, codigoPais],
                      (error, results, fields) => {
                        if (error) {
                          console.error('Error al actualizar el país:', error);
                          return;
                        }
                        // console.log(`País ${nombrePais} actualizado correctamente.`);
                        consultasCompletas++;
                      }
                    );                  
                }
                // console.log('Resultados de la consulta:', results);
              });
            }
          })
          .catch(error => {
            sinDatos.push(i)
            // console.error(`El código ${i} no retorna datos`, error.response.data.message);
            consultasCompletas++;

          })
      );
    }
  
    await Promise.all(promesas)
    .then(() => {
      // Imprimir inserciones, actualizaciones y códigos sin datos
      console.log(`Países insertados ${inserciones.length}:`, inserciones);
      console.log(`Países actualizados ${updates.length}:`, updates);
      console.log(`Códigos sin datos: ${sinDatos.length}`, sinDatos);

      // Verificar si todas las consultas se han completado antes de cerrar la conexión
      if (consultasCompletas === totalConsultas) {
        // Cerrar la conexión a la base de datos
        connection.end((err) => {
          if (err) {
            console.error('Error al cerrar la conexión:', err);
            return;
          }
          console.log('Conexión cerrada correctamente');
        });
      } else {
        console.log('Aún hay consultas pendientes, no se cerrará la conexión, total de consultas realizadas: ', consultasCompletas);
      }
    });
  };
  
