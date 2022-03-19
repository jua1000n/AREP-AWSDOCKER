import mongo.ConectMongo;

import static spark.Spark.*;

public class LogService {
    public static void main(String... args) {
        ConectMongo mongoDB = new ConectMongo();

        port(getPort());

        options("/*", (request, response) -> {
            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });

        before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));

        get("/savecadena", (req, res) -> {
            res.type("application/json");
            String value = req.queryParams("cadena");
            System.out.println("Esta entrando aca");
            if(value == null) {
                return null;
            } else {
                try{
                    return mongoDB.createConexion("AREP", value);
                }catch (NumberFormatException e) {
                    return null;
                }
            }
        });
    }

    private static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4567;
    }
}
