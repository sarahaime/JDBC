package servicios;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DB {


    private static DB instancia;
    private String URL = "jdbc:h2:tcp://localhost/~/practica3"; //Modo servidor...

    private  DB(){
        registrarDriver();
    }

    //singleton
    public static DB getInstancia(){
        if(instancia==null){
            instancia = new DB();
        }
        return instancia;
    }

    //Metodo para el registro de driver de conexión.
    private void registrarDriver() {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException ex) {
            System.out.print("Error al tratar de registrar driver");
            System.out.println(ex.getMessage());
        }
    }

    public Connection getConexion() {
        Connection con = null;
        try {
            //usuario y contrasenia por defecto
            con = DriverManager.getConnection(URL, "sa", "");
        } catch (SQLException ex) {
            System.out.print("Error al tratar de conectar");
            System.out.println(ex.getMessage());
        }
        return con;
    }

    public void testConexion() {
        try {
            getConexion().close();
            System.out.println("Conexión realizado con exito...");
        } catch (SQLException ex) {
            System.out.print("Fail :c");
            System.out.println(ex.getMessage());
        }
    }

}
