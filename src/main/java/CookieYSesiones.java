
import modelos.Usuario;
import servicios.UsuarioServices;
import spark.Session;

import java.util.Map;

import static spark.Spark.get;
import static spark.Spark.halt;
import static spark.Spark.post;

/**
 * Manejo de Cookies y Sesiones usando Spark.
 */
public class CookieYSesiones {

    public void cookieSesiones(){

        /**
         * http://localhost:4567/logout
         */
        get("/logout", (request, response)->{
            Session session=request.session(true);
            session.invalidate();
            response.cookie("/", "usuario", "hola_mundo", 0, false);


            response.redirect("/home");
            return "Adios amiguito";
        });

        /**
         * Registra elementos en el ambito web de sesion.
         */

        //HACER UNA COOKIE
      post("/autenticar", (request, response)->{
            Session session = request.session(true);
            UsuarioServices us = new UsuarioServices();
            Usuario usuario = us.autenticarUsuario(request.queryParams("user"), request.queryParams("pass"));

            if(usuario == null){
                response.redirect("/login");
                return "";
            }

            try {
                if("on".equalsIgnoreCase(request.queryParams("recordar"))){
                    response.cookie("/", "usuario", Integer.toString((int)usuario.getId()), 7*24*60*60*1000, false);  }
            }catch (Exception e){  }


          session.attribute("usuario", usuario);
          response.redirect("/home");
            return "";
        });

    }
}
