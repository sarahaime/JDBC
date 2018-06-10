package servicios;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import modelos.Usuario;
import org.h2.tools.Server;

public class BootStrapServices {

    public static void startDb() throws SQLException {
        Server.createTcpServer("-tcpPort", "9092", "-tcpAllowOthers").start();
    }

    public static void stopDb() throws SQLException {
        Server.shutdownTcpServer("tcp://localhost:9092", "", true, true);
    }


    /**
     * Metodo para crear las tablas necesarios
     */
    public static void crearTablas() throws  SQLException{
        Connection con = DB.getInstancia().getConexion();
        Statement statement = con.createStatement();
        String tablaUsuario = "CREATE TABLE IF NOT EXISTS USUARIO\n" +
                "(\n" +
                "  ID BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,\n" +
                "  NOMBRE VARCHAR(100) NOT NULL,\n" +
                "  USERNAME VARCHAR(100) NOT NULL,\n" +
                "  PASSWORD VARCHAR(100) NOT NULL,\n" +
                "  ADMINISTRADOR BOOLEAN,\n" +
                "  AUTOR BOOLEAN,\n" +
                ");";


        String tablaArticulo = "CREATE TABLE IF NOT EXISTS ARTICULO\n" +
                "(\n" +
                "  ID BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,\n" +
                "  TITULO VARCHAR(50) NOT NULL,\n" +
                "  CUERPO VARCHAR(255) NOT NULL,\n" +
                "  USUARIOID BIGINT NOT NULL,\n" +
                "  FECHA DATE NOT NULL DEFAULT NOW(),\n" +
                ");";

        String tablaComentario = "CREATE TABLE IF NOT EXISTS COMENTARIO\n" +
                "(\n" +
                "  ID BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,\n" +
                "  COMENTARIO TEXT NOT NULL,\n" +
                "  ARTICULOID BIGINT NOT NULL,\n" +
                "  USUARIOID BIGINT NOT NULL,\n" +
                "  FECHA DATE NOT NULL DEFAULT NOW()\n" +
                ");";

        String tablaEtiqueta = "CREATE TABLE IF NOT EXISTS ETIQUETA\n" +
                "(\n" +
                "  ID BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,\n" +
                "  ETIQUETA VARCHAR(80) NOT NULL,\n" +
                "  IDARTICULO BIGINT NOT NULL\n" +
                ");";

        statement.execute(tablaUsuario);
        statement.execute(tablaEtiqueta);
        statement.execute(tablaComentario);
        statement.execute(tablaArticulo);
        statement.close();
        //CIERRO, MUY IMPORTANTE
        con.close();

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
