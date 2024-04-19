package Garcia.Juan.database.migration;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MySQLToMongoDBMigrationEdadMin {
    public static void main(String[] args) {
        // Conexión a MySQL
        String mySqlUrl = "jdbc:mysql://localhost:3306/bibliotecapingu";
        String mySqlUser = "root";
        String mySqlPassword = "#32zvv48dH";

        try (Connection mySqlConnection = DriverManager.getConnection(mySqlUrl, mySqlUser, mySqlPassword)) {
            // Conexión a MongoDB
            String connectionString = "mongodb+srv://dm34mg:sTuFu28myI2b7Bs2@bibliotecapingu.vzsvhqa.mongodb.net/?retryWrites=true&w=majority&appName=Bibliotecapingu";
            MongoClient mongoClient = MongoClients.create(connectionString);
            MongoDatabase mongoDatabase = mongoClient.getDatabase("bibliotecapingu");
            MongoCollection<Document> collection = mongoDatabase.getCollection("edad_min");

            // Consulta para obtener datos de MySQL
            Statement statement = mySqlConnection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM edad_min");

            // Lista para guardar documentos
            List<Document> documents = new ArrayList<>();

            // Convertir registros a documentos
            while (resultSet.next()) {
                Document document = new Document()
                        .append("edad", resultSet.getInt("edad"))
                        .append("titulo", resultSet.getString("titulo"));

                documents.add(document);
            }

            // Insertar documentos en MongoDB
            collection.insertMany(documents);
            System.out.println("Migración completa para la tabla 'edad_min'");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
