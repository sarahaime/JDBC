import modelos.Usuario;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;

import static spark.Spark.*;
import static spark.Spark.before;
import static spark.Spark.halt;

public class Filtros { //para aplicar filtros
    public void aplicarFiltros(){

        before("/registrar*",(request, response) -> {
            Usuario usuario = request.session(true).attribute("usuario");
            if(usuario==null || !usuario.isAdministrador()){
                //parada del request, enviando un codigo.
                halt(401, "No tiene permisos para acceder");
            }
        });

        before("/comentar*",(request, response) -> {
            Usuario usuario = request.session(true).attribute("usuario");
            if(usuario==null){
                //parada del request, enviando un codigo.
                halt(401, "No tiene permisos para acceder..");
            }
        });


        before("/usuario/eliminar/*",(request, response) -> {
            Usuario usuario = request.session(true).attribute("usuario");
            if(usuario==null || !usuario.isAdministrador()){
                //parada del request, enviando un codigo.
                halt(401, "No tiene permisos para acceder");
            }
        });


        before("/articulo/modificar/*",(request, response) -> {
            Usuario usuario = request.session(true).attribute("usuario");
            if(usuario==null || (  !usuario.isAdministrador() &&  !usuario.isAutor()  )){
                //parada del request, enviando un codigo.
                halt(401, "No tiene permisos para acceder");
            }
        });

        before("/comentar",(request, response) -> {
            Usuario usuario = request.session(true).attribute("usuario");

            if(usuario == null){
                //parada del request, enviando un codigo.
                response.redirect("/login.html");
                halt(200, "No tiene permisos para acceder..");
            }

            System.out.println(usuario.getUsername());

        });
    }



}
