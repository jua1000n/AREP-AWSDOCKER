package mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.function.Consumer;

public class ConectMongo {
    private String url = "db";
    //private String url = "localhost";
    private int port = 27017;
    private MongoClient mongo = null;
    private MongoDatabase prueba = null;
    private MongoCollection<Document> collection;

    public String createConexion(String nameDB, String cadena) {

        try{
            System.out.println("url");
            mongo = new MongoClient(url);
            prueba = mongo.getDatabase(nameDB);
            collection = prueba.getCollection("DateCadena");

            setNewCadena(cadena);
            //return "";
            return getCadenas();
        }catch (MongoException e) {
            System.out.println("Error en la conexion"+ e.toString());
            return "";
        }
    }

    public void setNewCadena(String cadena) {
        Calendar actual = Calendar.getInstance();
        String fecha = actual.get(Calendar.YEAR)+ "-" +actual.get(Calendar.MONTH)+ "-" +actual.get(Calendar.DAY_OF_MONTH);
        Document dateCadena = new Document("cadena", cadena).append("fecha", fecha);

        collection.insertOne(dateCadena);
    }

    public String getCadenas() {
        ArrayList<String> listCadena = new ArrayList<>();
        FindIterable<Document> result = collection.find();
        result.forEach((Consumer<? super Document>) d -> listCadena.add(d.toJson()));
        String res = "]";
        if(listCadena.size()>10) {
            for (int i = (listCadena.size()-10); i< listCadena.size()-1; i++) {
                res= ", "+ listCadena.get(i) + res;
            }
            res= "["+ listCadena.get(listCadena.size()-1)+res;

        }else {
            for (int i = 0; i< listCadena.size()-1; i++) {
                res= ", "+ listCadena.get(i) + res;
            }
            res= "["+ listCadena.get(listCadena.size()-1)+res;
        }
        return res;
    }

}
