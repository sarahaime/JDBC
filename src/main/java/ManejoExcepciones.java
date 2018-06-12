import static spark.Spark.exception;
import static spark.Spark.get;

public class ManejoExcepciones {


    public void manejoExcepciones(){

        /**
         * La excepción que se genere del tipo NumberFormatException, será atrapada
         * por este bloque.
         */
        exception(NumberFormatException.class, (e, request, response) -> {
            response.status(500);
            response.body("Error convertiendo un número....");
            e.printStackTrace();
        });

        //otras
        exception(Exception.class, (e, request, response) -> {
            response.status(500);
            response.body(e.getMessage());
            e.printStackTrace();
        });


    }
}
