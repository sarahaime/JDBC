package servicios;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import org.h2.tools.Server;

public class BootStrapServices {

    /**
     *
     * @throws SQLException
     */
    public static void startDb() throws SQLException {
        Server.createTcpServer("-tcpPort", "9092", "-tcpAllowOthers").start();
    }

    /**
     *
     * @throws SQLException
     */
    public static void stopDb() throws SQLException {
        Server.shutdownTcpServer("tcp://localhost:9092", "", true, true);
    }


    /**
     * Metodo para recrear las tablas necesarios
     * @throws SQLException
     */
    public static void crearTablas() throws  SQLException{
        Connection con = DB.getInstancia().getConexion();
        Statement statement = con.createStatement();
        String tablaUsuario = "CREATE TABLE IF NOT EXISTS USUARIO\n" +
                "(\n" +
                "  ID INTEGER AUTO_INCREMENT PRIMARY KEY NOT NULL,\n" +
                "  NOMBRE VARCHAR(100) NOT NULL,\n" +
                "  USERNAME VARCHAR(100) NOT NULL,\n" +
                "  PASSWORD VARCHAR(100) NOT NULL,\n" +
                "  ADMINISTRADOR BOOLEAN,\n" +
                "  AUTOR BOOLEAN,\n" +
                ");";


        String tablaArticulo = "CREATE TABLE IF NOT EXISTS ARTICULO\n" +
                "(\n" +
                "  ID INTEGER AUTO_INCREMENT PRIMARY KEY NOT NULL,\n" +
                "  TITULO VARCHAR(50) NOT NULL,\n" +
                "  CUERPO VARCHAR(255) NOT NULL,\n" +
                "  USUARIOID INTEGER NOT NULL,\n" +
                "  FECHA DATE NOT NULL DEFAULT NOW(),\n" +
                ");";

        String tablaComentario = "CREATE TABLE IF NOT EXISTS COMENTARIO\n" +
                "(\n" +
                "  ID INTEGER AUTO_INCREMENT PRIMARY KEY NOT NULL,\n" +
                "  COMENTARIO TEXT NOT NULL,\n" +
                "  ARTICULOID INTEGER NOT NULL,\n" +
                "  USUARIOID INTEGER NOT NULL,\n" +
                "  FECHA DATE NOT NULL DEFAULT NOW()\n" +
                ");";

        String tablaEtiqueta = "CREATE TABLE IF NOT EXISTS ETIQUETA\n" +
                "(\n" +
                "  ID INTEGER AUTO_INCREMENT PRIMARY KEY NOT NULL,\n" +
                "  ETIQUETA VARCHAR(80) NOT NULL,\n" +
                "  IDARTICULO INTEGER NOT NULL\n" +
                ");";

        String registroAdministrador = "INSERT INTO USUARIO(username, nombre, password, administrador, autor)\n" +
                "VALUES ('sarahaime','Sarahaime Rodríguez Torres', 'sara',true, false);";


        statement.execute(tablaUsuario);
        statement.execute(tablaEtiqueta);
        statement.execute(tablaComentario);
        statement.execute(tablaArticulo);




        statement.close();
        //CIERRO, MUY IMPORTANTE
        con.close();
    }

}
