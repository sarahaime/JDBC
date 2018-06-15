
import modelos.Articulo;
import modelos.Etiqueta;
import modelos.Usuario;
import servicios.ArticuloServices;
import servicios.ComentarioServices;
import servicios.UsuarioServices;

import static spark.Spark.*;

import spark.*;
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
            Usuario usuario = new Usuario();
            Session session = request.session(true);
            List<Articulo> listaArticulos = as.listaArticulos();
            Map<String, Object> modelo = new HashMap<>();
            modelo.put("listaArticulos", listaArticulos);

            if(request.cookie("usuario") != null){
                UsuarioServices us = new UsuarioServices();
                usuario = us.getUsuario(Integer.parseInt(request.cookie("usuario")));
                session.attribute("usuario", usuario);
            }

            if(session.attribute("usuario") != null) usuario = session.attribute("usuario");

            modelo.put("registeredUser", usuario);

            return renderThymeleaf(modelo,"/home");
        });

        get("/registrar", (request, response) -> {
            Usuario u = new Usuario();
            Map<String, Object> modelo = new HashMap<>();
            modelo.put("usuario", u);
            modelo.put("registeredUser", getLogUser(request));

            return renderThymeleaf(modelo,"/registrar");
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
            us.crearUsuario(usuario);

            response.redirect("/registrar");
            return "";
        });


        get("/login", (request, response)->{
            Map<String, Object> modelo = new HashMap<>();
            return renderThymeleaf(modelo, "/login");
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
            modelo.put("registeredUser", getLogUser(request));

            return renderThymeleaf(modelo, "/articulo");
        });

        /**
         * por si no ponen /home
         */
        get("",  (request, response) -> {
            response.redirect("/home");
            return "";
        });

        get("/",  (request, response) -> {
            response.redirect("/home");
            return "";
        });



        post("/comentar", (request, response) -> {
            ComentarioServices cs = new ComentarioServices();
            Session session = request.session(true);
            int id = Integer.parseInt(request.queryParams("articuloid"));
            int usuarioid = (int)( (Usuario)session.attribute("usuario")).getId();
            cs.crearComentario(request.queryParams("comentario"), (long)usuarioid, (long)id);
            response.redirect("/ver?id="+id);
            return "";
        });

        post("/borrarArticulo", (request, response) -> {
            ArticuloServices as = new ArticuloServices();
            Session session = request.session(true);
            int articuloid = Integer.parseInt(request.queryParams("articuloid"));
            int usuarioid = (int)( (Usuario)session.attribute("usuario")).getId();
            as.borrarArticulo(articuloid, usuarioid);
            response.redirect("/home");
            return "";
        });

        post("/agregarArticulo", (request, response) -> {
            ArticuloServices as = new ArticuloServices();
            Session session = request.session(true);

            int usuarioid = (int)( (Usuario)session.attribute("usuario")).getId();

            String titulo = request.queryParams("titulo");
            String cuerpo = request.queryParams("cuerpo");

            String tags = request.queryParams("etiquetas");

            as.crearArticulo(titulo, cuerpo, usuarioid, tags);

            response.redirect("/home");
            return "";
        });

        get("/editarArticulo", (request, response)->{
            int id = Integer.parseInt(request.queryParams("id"));
            ArticuloServices as = new ArticuloServices();
            Articulo articulo = as.getArticulo((long) id);
            Map<String, Object> modelo = new HashMap<>();
            modelo.put("registeredUser", getLogUser(request));
            modelo.put("articulo", articulo);
            String etiqueta = "";

            for(Etiqueta et: articulo.getEtiquetas()){
                etiqueta += et.getEtiqueta() + ", ";
            }

            modelo.put("etiqueta", etiqueta);

            return renderThymeleaf(modelo, "/editarArticuloForm");
        });

        post("/editarArticulo", (request, response) -> {
            ArticuloServices as = new ArticuloServices();
            String titulo = request.queryParams("titulo");
            String cuerpo = request.queryParams("cuerpo");
            String tags = request.queryParams("etiquetas");
            long id = (long) Integer.parseInt( request.queryParams("id"));

            System.out.println(tags);
            as.actualizarArticulo(titulo, cuerpo, id, tags);

            response.redirect("/home");
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

    private Usuario getLogUser(Request request){

            Usuario usuario = new Usuario();
            Session session = request.session(true);

            if(request.cookie("usuario") != null){
                UsuarioServices us = new UsuarioServices();
                usuario = us.getUsuario(Integer.parseInt(request.cookie("usuario")));
                session.attribute("usuario", usuario);
            }

            if(session.attribute("usuario") != null) usuario = session.attribute("usuario");




        return usuario;
    }



}
