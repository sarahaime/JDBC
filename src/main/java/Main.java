import modelos.Usuario;
import servicios.BootStrapServices;
import servicios.DB;
import servicios.UsuarioServices;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args)throws SQLException {
        //Iniciando el servicio
        BootStrapServices.startDb();

        //Prueba de Conexión.
        DB.getInstancia().testConexion();


        BootStrapServices.crearTablas();


        //usuario administrador por defecto
        UsuarioServices usuarioServices = new UsuarioServices();
        if(usuarioServices.listaUsuarios().size() < 1){
            Usuario administrador = new Usuario();
            administrador.setNombre("Admin");
            administrador.setUsername("admin");
            administrador.setAdministrador(true);
            administrador.setAutor(false);
            administrador.setPassword("admin");
            if( usuarioServices.crearUsuario(administrador)){
                System.out.println("Usuario administrador creado..");
            }
        }



    }
}
