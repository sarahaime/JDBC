
import modelos.Articulo;
import modelos.Usuario;
import servicios.ArticuloServices;
import servicios.ComentarioServices;
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


        get("/registrar", (request, response) -> {
            Usuario u = new Usuario();
            Map<String, Object> modelo = new HashMap<>();
            modelo.put("usuario", u);

            return renderThymeleaf(modelo,"/registrar");
        });


        get("/ver", (request, response)->{
            int id = Integer.parseInt(request.queryParams("id"));
            ArticuloServices as = new ArticuloServices();
            Articulo articulo = as.getArticulo((long) id);

            Map<String, Object> modelo = new HashMap<>();
            modelo.put("articulo", articulo);
            modelo.put("comentarios", articulo.getComentarios());
            modelo.put("autor", articulo.getAutor().getNombre());
            modelo.put("etiquetas", articulo.getEtiquetas());
            return renderThymeleaf(modelo, "/articulo");
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

        post("/registrar", (request, response) -> {
            Usuario usuario = new Usuario();
            usuario.setNombre(request.queryParams("nombre"));
            usuario.setUsername(request.queryParams("username"));
            usuario.setPassword(request.queryParams("password"));
            try {
                usuario.setAdministrador("on".equalsIgnoreCase(request.queryParams("administrador")));
            }catch (Exception e){
            }
            try {
                usuario.setAutor("on".equalsIgnoreCase(request.queryParams("autor")));
            }catch (Exception e){
            }

            UsuarioServices us = new UsuarioServices();
            System.out.println(us.crearUsuario(usuario));
            response.redirect("/registrar");
            return "";
        });


        post("/comentar", (request, response) -> {
            ComentarioServices cs = new ComentarioServices();
            int id = Integer.parseInt(request.queryParams("articuloid"));
            //usuarioid
            System.out.println(cs.crearComentario(request.queryParams("comentario"), (long)1, (long)id));
            response.redirect("/ver?id="+id);
            return "";
        });


    }

    private static Object procesarParametros(Request request, Response response){
      //  System.out.println("Recibiendo mensaje por el metodo: "+request.requestMethod());
        Set<String> parametros = request.queryParams();
        String salida="";

        for(String param : parametros){
            salida += String.format("Parametro[%s] = %s <br/>", param, request.queryParams(param));
        }

        return salida;
    }



}
