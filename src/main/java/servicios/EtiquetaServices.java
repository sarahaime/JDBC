package servicios;

import modelos.Etiqueta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EtiquetaServices{
    public boolean crearEtiqueta(String etiqueta){
        boolean ok =false;
        Connection con = null;
        try {
            String query = "insert into ETIQUETA (ETIQUETA) values(?)";
            con = DB.getInstancia().getConexion();
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);
            //Antes de ejecutar seteo los parametros
            prepareStatement.setString(1, etiqueta);
            int fila = prepareStatement.executeUpdate();
            ok = fila > 0 ;
        } catch (SQLException ex) {
            Logger.getLogger(EtiquetaServices.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(EtiquetaServices.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return ok;
    }
    public static List<Etiqueta> getEtiquetaByArticuloID(long articuloID){
        List<Etiqueta> lista = new ArrayList<>();
        Connection con = null; //objeto conexion.
        try {
            String query = "select * from ETIQUETA  where ARTICULOID = ?";
            con = DB.getInstancia().getConexion();
            PreparedStatement prepareStatement = con.prepareStatement(query);
            //Antes de ejecutar seteo los parametros.
            prepareStatement.setLong(1, articuloID);

            ResultSet rs = prepareStatement.executeQuery();
            while(rs.next()){
                long id = rs.getLong("id");
                lista.add(getEtiquetaByID(id));
            }

        } catch (SQLException ex) {
            Logger.getLogger(EtiquetaServices.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(EtiquetaServices.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return lista;

    }
    public boolean agregarEtiquetaArticulo(long etiquetaID, long articuloID){
        boolean ok =false;
        Connection con = null;
        try {
            String query = "insert into ETIQUETA (ETIQUETAID, ARTICULOID) values(?, ?)";
            con = DB.getInstancia().getConexion();
            PreparedStatement prepareStatement = con.prepareStatement(query);
            //Antes de ejecutar seteo los parametros
            prepareStatement.setLong(1, etiquetaID);
            prepareStatement.setLong(2, articuloID);
            int fila = prepareStatement.executeUpdate();
            ok = fila > 0 ;
        } catch (SQLException ex) {
            Logger.getLogger(EtiquetaServices.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(EtiquetaServices.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return ok;
    }
    public static Etiqueta getEtiquetaByID(long id){
        Etiqueta etiqueta = null;
        Connection con = null;
        try {
            //utilizando los comodines (?)...
            String query = "select * from ETIQUETA where id = ?";
            con = DB.getInstancia().getConexion();
            PreparedStatement prepareStatement = con.prepareStatement(query);
            //Antes de ejecutar seteo los parametros.
            prepareStatement.setLong(1, id);
            //Ejecuto...
            ResultSet rs = prepareStatement.executeQuery();
            while(rs.next()){
                etiqueta = new Etiqueta();
                etiqueta.setId(rs.getLong("id"));
                etiqueta.setEtiqueta(rs.getString("etiqueta"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(EtiquetaServices.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(EtiquetaServices.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return etiqueta;

    }

}
