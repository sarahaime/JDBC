package servicios;

import modelos.Comentario;
import modelos.Comentario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ComentarioServices {
    public static boolean crearComentario(String comentario, long usuarioId, long articuloId){
        boolean ok =false;
        Connection con = null;
        try {
            String query = "insert into COMENTARIO (COMENTARIO, USUARIOID, ARTICULOID) values(?,?,?)";
            con = DB.getInstancia().getConexion();
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);
            //Antes de ejecutar seteo los parametros
            prepareStatement.setString(1, comentario);
            prepareStatement.setLong(2, usuarioId);
            prepareStatement.setLong(3, articuloId);
            int fila = prepareStatement.executeUpdate();
            ok = fila > 0 ;

        } catch (SQLException ex) {
            Logger.getLogger(UsuarioServices.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(UsuarioServices.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return ok;

    }
    public static List<Comentario> getComentarioByArticuloID(long articuloID){
        List<Comentario> lista = new ArrayList<>();
        Connection con = null; //objeto conexion.
        try {

            String query = "select * from COMENTARIO where ARTICULOID = ?";

            con = DB.getInstancia().getConexion(); //referencia a la conexion.

            PreparedStatement prepareStatement = con.prepareStatement(query);
            prepareStatement.setLong(1, articuloID);
            ResultSet rs = prepareStatement.executeQuery();
            while(rs.next()){
                Comentario comentario = new Comentario();
                comentario.setId(rs.getLong("id"));
                comentario.setComentario(rs.getString("comentario"));
                comentario.setAutor( UsuarioServices.getUsuario( rs.getLong("USUARIOID") ) );
                lista.add(comentario);
            }

        } catch (SQLException ex) {
            Logger.getLogger(UsuarioServices.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(UsuarioServices.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return lista;

    }
}
