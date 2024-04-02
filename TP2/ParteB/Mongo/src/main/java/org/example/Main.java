package org.example;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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
            String codigoPais = "";
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
//                                    codigoPais = callingCodesArray.getString(0);
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


            // Create the document to specify find criteria
            Document findDocument51 = new Document("region", "Americas");
            // Document to store query results
            FindIterable<Document> resultDocuments51 = paises.find(findDocument51);
            // Iterate over the result documents and print each one
            for (Document doc : resultDocuments51) {
                System.out.println(doc.toJson());
            }

            Document findDocument52 = new Document();
            findDocument52.append("region", "Americas");
            findDocument52.append("poblacion", new Document("$gt", 10000000)); // "$gt" representa el operador mayor que

            // Documento para almacenar los resultados de la consulta
                        FindIterable<Document> resultDocuments = paises.find(findDocument52);

            // Iterar sobre los documentos de resultado y mostrar cada uno
            for (Document doc : resultDocuments) {
                System.out.println(doc.toJson());
            }

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
}