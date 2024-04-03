package org.example;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.print.Doc;
import java.net.http.HttpClient;
import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        System.out.println("Prueba conexión MongoDB");
        MongoClient mongoClient = crearConexion();

        if (mongoClient != null) {
            System.out.println("Conexion Exitosa");
            List<String> databases = mongoClient.getDatabaseNames();

            for (String dbName : databases) {
                System.out.println("Database: " + dbName);

                DB db = mongoClient.getDB(dbName);

                Set<String> collections = db.getCollectionNames();
                for (String colName : collections) {
                    System.out.println("\t + Collection: " + colName);
                }
            }

            MongoDatabase database = mongoClient.getDatabase("paises_db");
            MongoCollection<Document> paises = database.getCollection("paises");

            int positivas = 0;
            int negativas = 0;

            // Crear un cliente HTTP
            HttpClient httpClient = HttpClient.newHttpClient();
            String name = "";
            String capital = "";
            String region = "";
            int codigoPais = 0;
            double poblacion = 0;
            double latitud = 0;
            double longitud = 0;

//            for (int i = 1; i <= 300; i++){
//                // Definir la URL de la API a consumir
//                String apiUrl = "https://restcountries.com/v2/callingcode/"+i;
//
//                // Crear una solicitud HTTP GET
//                HttpRequest request = HttpRequest.newBuilder()
//                        .uri(URI.create(apiUrl))
//                        .build();
//
//                // Enviar la solicitud y procesar la respuesta
//                try {
//                    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
//
//                    // Verificar si la respuesta es exitosa (código de estado 200)
//                    if (response.statusCode() == 200) {
//                        positivas++;
//                        // Imprimir el cuerpo de la respuesta
////                        System.out.println("Respuesta de la API:");
//                        String responseBody = response.body();
//                        JSONArray jsonArray = new JSONArray(responseBody);
////                        System.out.println(jsonArray);
//                        for (int j = 0; j < jsonArray.length(); j++) {
//                            JSONObject jsonObject = jsonArray.getJSONObject(j);
//                            if (jsonObject.has("name")) {
//                                name = jsonObject.getString("name");
//                            }
//                            if (jsonObject.has("capital")){
//                                capital = jsonObject.getString("capital");
//                            }
//                            if (jsonObject.has("region")){
//                                region = jsonObject.getString("region");
//                            }
//                            if (jsonObject.has("region")){
//                                poblacion = jsonObject.getDouble("population");
//                            }
//                            if (jsonObject.has("latlng")) {
//                                JSONArray latlngArray = jsonObject.getJSONArray("latlng");
//                                if (latlngArray.length() >= 2) {
//                                    latitud = latlngArray.getDouble(0);
//                                    longitud = latlngArray.getDouble(1);
//                                }
//                            }
//                            if (jsonObject.has("callingCodes")){
//                                JSONArray callingCodesArray = jsonObject.getJSONArray("callingCodes");
//                                if (callingCodesArray.length() >= 1) {
//                                    codigoPais = callingCodesArray.getInt(0);
//                                    // Verificar si el documento ya existe en la colección
//                                    Document existingDoc = paises.find(new Document("codigoPais", codigoPais)).first();
//                                    if (existingDoc != null) {
//                                        // El documento ya existe, por lo tanto, actualiza los datos
//                                        Document updateDoc = new Document("$set", new Document("codigoPais", codigoPais)
//                                                .append("capitalPais", capital)
//                                                .append("region", region)
//                                                .append("poblacion", poblacion)
//                                                .append("latitud", latitud)
//                                                .append("longitud", longitud));
//                                        paises.updateOne(new Document("codigoPais", codigoPais), updateDoc);
//                                    } else {
//                                        // El documento no existe, por lo tanto, insértalo en la colección
//                                        Document pais = new Document("_id", new ObjectId())
//                                                .append("nombrePais", name )
//                                                .append("capitalPais", capital)
//                                                .append("region", region)
//                                                .append("poblacion", poblacion)
//                                                .append("latitud", latitud)
//                                                .append("longitud", longitud)
//                                                .append("codigoPais", codigoPais);
//
//                                        // Inserta el documento en la colección
//                                        paises.insertOne(pais);
//                                    }
//                                }
//
//                            }
//
//                        }
//
//                    } else {
//                        negativas++;
////                        System.out.println("La solicitud falló con el código de estado: " + response.statusCode());
//                    }
//                } catch (Exception e) {
//                    System.err.println("Error al enviar la solicitud: " + e.getMessage() + i);
//                }
//
//            }
            System.out.println(positivas);
            System.out.println(negativas);

            //Llamada a los metodos
            //5.1
            System.out.println("Documentos con region igual a Americas");
            buscarPorRegion(paises);
            //5.2
            System.out.println("Documentos con region igual a Americas y poblacion mayor a 100000000");
            buscarPorRegionYPoblacion(paises);
            //5.3
            System.out.println("Documentos con region distinta a Africa");
            buscarPorRegionDistinta(paises);
            //5.4
            actualizarDocumento(paises);
            //5.5
            eliminarDocumento(paises);
            //5.7
            System.out.println("Documentos con poblacion mayor a 50000000 y menor a 150000000");
            seleccionPorPoblacion(paises);
            //5.8
            System.out.println("Documentos ordenador por nombre en forma ascendente");
            ordenarPorNombre(paises);
            //5.9
            System.out.println("Salto 5 elementos antes de obtener los resultados");
            usoSkip(paises);
            //5.10
            System.out.println("Expresiones regulares para buscar documentos con nombres que contengan -Ar-");
            expresionesRegulares(paises);
        } else {
            System.out.println("Error: Conexión no establecida");
        }
    }

    private static MongoClient crearConexion() {
        MongoClient mongo = null;
        try {
            mongo = new MongoClient("localhost", 27017);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mongo;
    }
    // punto 5.1
    private static void buscarPorRegion(MongoCollection<Document> paises) {
        String region = "Americas";
        // Crear el documento para especificar los criterios de búsqueda
        Document filtro = new Document("region", region);

        // Documento para almacenar los resultados de la consulta
        FindIterable<Document> resultados = paises.find(filtro);

        // Iterar sobre los documentos de resultado y mostrar cada uno
        for (Document doc : resultados) {
            System.out.println(doc.toJson());
        }
    }

    // punto 5.2
    private static void buscarPorRegionYPoblacion(MongoCollection<Document> paises) {
        String region = "Americas";
        int poblacionMinima = 100000000;
        // Crear el documento para especificar los criterios de búsqueda
        Document filtro = new Document();
        filtro.append("region", region);
        // "$gt" representa el operador mayor que
        filtro.append("poblacion", new Document("$gt", poblacionMinima));

        // Documento para almacenar los resultados de la consulta
        FindIterable<Document> resultados = paises.find(filtro);

        // Iterar sobre los documentos de resultado y mostrar cada uno
        for (Document doc : resultados) {
            System.out.println(doc.toJson());
        }
    }
    //punto 5.3
    public static void buscarPorRegionDistinta(MongoCollection<Document> paises){
        String region = "Africa";
        // Crear el documento para especificar los criterios de búsqueda
        // "$ne" representa el operador distinto de
        Document filtro = new Document("region", new Document("$ne", region));

        // Documento para almacenar los resultados de la consulta
        FindIterable<Document> resultados = paises.find(filtro);

        // Iterar sobre los documentos de resultado y mostrar cada uno
        for(Document doc : resultados){
            System.out.println(doc.toJson());
        }
    }

    // punto 5.4
    public static void actualizarDocumento(MongoCollection<Document> paises){
        String name = "Egypt";
        Document filtro = new Document("name", name);

        Document cambios = new Document("$set", new Document("name", "Egipto").append("poblacion", 95000000));

        // hacemos uso de updateOne porque la consigna dice que solo hay que actualizar el documento y no los documentos
        // para actualizar mas de uno debemos hacer uso de updateMany
        paises.updateOne(filtro, cambios);

        System.out.println("Documento actualizado con éxito.");

    }

    // punto 5.5
    public static void eliminarDocumento(MongoCollection<Document> paises){
        int codigoPais = 258;
        Document filtro = new Document("codigoPais", codigoPais);

        // utilizamos deleteOne para eliminar solo un documento
        paises.deleteOne(filtro);

    }
    //5.6
    // Al ejecutar drop() sobre una colección, eliminaremos esa coleccion  de la base de datos y los documentos que contenga
    // la misma.
    // Y al ejecutar el método drop() sobre una base de datos, se elimina completamente esa
    // base de datos y todas las colecciones que contiene.
    // El metodo drop() es una operación irreversible y una vez realizada no podremos recuperar los datos eliminados
    // Por esa razón es importante hacer un backup de la coleccion o base de datos a la que vamos a realizarle este método.


    // punto 5.7
    public static void seleccionPorPoblacion(MongoCollection<Document> paises){
        int pobMayorA = 50000000;
        int pobMenorA = 150000000;
        Document filtro = new Document();
        // "$gt" representa el operador mayor que
        filtro.append("poblacion", new Document("$gt", pobMayorA));
        // "$lt" representa el operador menor que
        filtro.append("poblacion", new Document("$lt", pobMenorA));

        // Realiza la consulta
        FindIterable<Document> resultados = paises.find(filtro);

        // Iterar sobre los documentos de resultado y mostrar cada uno
        for (Document doc : resultados) {
            System.out.println(doc.toJson());
        }
    }
    //5.8
    private static void ordenarPorNombre(MongoCollection<Document> paises){
        Document filtro = new Document();
        // Ordenar los documentos por el campo "name" en forma ascendente
        Document orden = new Document("name", 1); // "1" para orden ascendente, "-1" para orden descendente

        // Realizar la consulta para seleccionar y ordenar los documentos
        FindIterable<Document> resultados = paises.find(filtro).sort(orden);

        // Iterar sobre los documentos de resultado y mostrar cada uno
        for (Document doc : resultados) {
            System.out.println(doc.toJson());
        }
    }
    //5.9
    public static void usoSkip(MongoCollection<Document> paises){
        int numElements = 5;

        // Al ejecutar el método skip() sobre una colección podemos omitir resultados y no
        // recuperar cierta cantidad de documentos. Debemos pasarle como parámetro la cantidad
        // de elementos que queremos saltar en nuestra búsqueda. En el ejemplo vamos a omitir los primeros
        // 5 documentos de la coleccion.

        // Realiza la consulta
        FindIterable<Document> resultados = paises.find().skip(numElements);

        // Iterar sobre los documentos de resultado y mostrar cada uno
        for (Document doc : resultados) {
            System.out.println(doc.toJson());
        }
    }
    //5.10
    public static void expresionesRegulares(MongoCollection<Document> paises){
        Document filtro = new Document("name", new Document("$regex", ".*Ar.*"));

        // Realiza la consulta
        FindIterable<Document> resultados = paises.find(filtro);

        // Itera sobre los documentos de resultado y muestra cada uno
        for (Document doc : resultados) {
            System.out.println(doc.toJson());
        }
    }
    //5.11


}