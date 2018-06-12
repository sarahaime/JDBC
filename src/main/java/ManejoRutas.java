
import modelos.Articulo;
import servicios.ArticuloServices;
import servicios.UsuarioServices;

import static spark.Spark.*;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;





public class ManejoRutas {

    // Declaración para simplificar el uso del motor de template Thymeleaf.
    public static String renderThymeleaf(Map<String, Object> model, String templatePath) {
        return new ThymeleafTemplateEngine().render(new ModelAndView(model, templatePath));
    }


    public void rutas(){

        get("/home", (request, response) -> {
            ArticuloServices as = new ArticuloServices();
            List<Articulo> listaArticulos = as.listaArticulos();
            Map<String, Object> modelo = new HashMap<>();
            modelo.put("listaArticulos", listaArticulos);

            return renderThymeleaf(modelo,"/home");
        });

        get("/articulos/", (request, reponse) -> {
            ArticuloServices as = new ArticuloServices();
            return as.listaArticulos();
        });

        get("/articulos/:id", (request, response)->{
            long id =Integer.parseInt(request.params("id"));
            ArticuloServices as = new ArticuloServices();
            Articulo articulo = as.getArticulo(id);
            return articulo;
        });

        /**
         * dentro de API, tendremos el manejo de usuario.
         * http://localhost:4567/api/usuario/
         */
        path("/usuario", () -> {
            get("/",      (request, response) -> "API Usuario");
            get("/lista/",      (request, response) ->{
                UsuarioServices us = new UsuarioServices();
                return  us.listaUsuarios();
            });
            post("/crear",      (request, response) -> "Agregando Usuario");
            put("/modificar",     (request, response) -> "Modificando Usuario");
            delete("/eliminar", (request, response) -> "Eliminando Usuario");
        });

        /**
         * dentro de API, tendremos el manejo de articulo.
         * http://localhost:4567/api/articulo/
         */
        path("/articulo", () -> {
            get("/",      (request, response) -> "API Articulo");
            get("/lista/",      (request, response) ->{
                ArticuloServices ar = new ArticuloServices();
                return  ar.listaArticulos();
            });
            post("/crear",      (request, response) -> "Agregando Articulo");
            put("/modificar",     (request, response) -> "Modificando Articulo");
            delete("/eliminar", (request, response) -> "Eliminando Eliminando");
        });



    }
}
