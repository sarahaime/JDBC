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
        String tablaUsuario ="CREATE TABLE IF NOT EXISTS USUARIO\n" +
                "(\n" +
                "  ID BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,\n" +
                "  NOMBRE VARCHAR(255) NOT NULL,\n" +
                "  USERNAME VARCHAR(255) NOT NULL,\n" +
                "  PASSWORD TEXT NOT NULL,\n" +
                "  ADMINISTRADOR BOOLEAN DEFAULT false ,\n" +
                "  AUTOR BOOLEAN DEFAULT false,\n" +
                ");";

        String tablaArticulo = "CREATE TABLE IF NOT EXISTS ARTICULO\n" +
                "(\n" +
                "  ID BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,\n" +
                "  TITULO VARCHAR(255) NOT NULL,\n" +
                "  CUERPO TEXT NOT NULL,\n" +
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
                "  ETIQUETA TEXT NOT NULL,\n" +
                "  ARTICULOID BIGINT NOT NULL\n" +
                ");";


        statement.execute(tablaUsuario);
        statement.execute(tablaEtiqueta);
        statement.execute(tablaComentario);
        statement.execute(tablaArticulo);
        statement.close();
        //CIERRO, MUY IMPORTANTE
        con.close();
    }

}
