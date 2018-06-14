
import modelos.Articulo;
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
            List<Articulo> listaArticulos = as.listaArticulos();
            Map<String, Object> modelo = new HashMap<>();
            modelo.put("listaArticulos", listaArticulos);
            modelo.put("registeredUser", getLogUser(request));
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

            System.out.println(tags);
            as.crearArticulo(titulo, cuerpo, usuarioid);

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
            return renderThymeleaf(modelo, "/editarArticuloForm");
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
        UsuarioServices us = new UsuarioServices();
        Session session = request.session(true);
        if(request.cookie("usuario") != null){
            usuario = us.getUsuario(Integer.parseInt(request.cookie("usuario")));
            if( usuario.getPassword().equals(request.cookie("usuario")) ) {
                session.attribute("usuario", usuario);
            }
        }else{
            if( session.attribute("usuario") instanceof Usuario)
          usuario = us.getUsuarioByUserName(((Usuario)session.attribute("usuario")).getUsername());
        }


        return usuario;
    }



}
