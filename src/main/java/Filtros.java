import modelos.Articulo;
import modelos.Usuario;
import servicios.ArticuloServices;
import servicios.UsuarioServices;
import spark.Session;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;

import static spark.Spark.*;
import static spark.Spark.before;
import static spark.Spark.halt;

public class Filtros { //para aplicar filtros
    public void aplicarFiltros(){

        before("/registrar",(request, response) -> {
            Usuario usuario = request.session(true).attribute("usuario");
            if(usuario==null || !usuario.isAdministrador()){
                //parada del request, enviando un codigo.
                response.redirect("/login");
                halt(401, "No tiene permisos para registrar");
            }
        });

        before("/comentar",(request, response) -> {
            Usuario usuario = request.session(true).attribute("usuario");
            if(usuario==null){
                //parada del request, enviando un codigo.
                response.redirect("/login");
                halt(401, "No tiene permisos para acceder..");
            }
        });


        before("/usuario/eliminar/*",(request, response) -> {
            Usuario usuario = request.session(true).attribute("usuario");
            if(usuario==null || !usuario.isAdministrador()){
                //parada del request, enviando un codigo.
                response.redirect("/login");
                halt(401, "No tiene permisos para acceder");
            }
        });


        before("/editarArticulo",(request, response) -> {
            Session session = request.session();
            int id = Integer.parseInt(request.queryParams("id"));
            ArticuloServices as = new ArticuloServices();
            Articulo articulo = as.getArticulo((long) id);
            if(articulo.getAutor().getId() !=  (int)( (Usuario)session.attribute("usuario")).getId()){
                response.redirect("/login");
                halt(200, "No tiene permisos para editar, solo el autor..");
            }
        });

        before("/borrarArticulo",(request, response) -> {
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
          //  System.out.println(usuario.getUsername());
        });



    }



}
