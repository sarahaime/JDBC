import servicios.BootStrapServices;
import servicios.DB;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args)throws SQLException {
        //Iniciando el servicio
        BootStrapServices.startDb();

        //Prueba de Conexión.
        DB.getInstancia().testConexion();

        BootStrapServices.crearTablas();

    }
}
