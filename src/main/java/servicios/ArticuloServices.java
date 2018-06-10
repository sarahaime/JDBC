package servicios;

import modelos.Articulo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ArticuloServices {
    public List<Articulo> listaArticulos() {
        List<Articulo> lista = new ArrayList<>();
        Connection con = null; //objeto conexion.
        try {
            String query = "select * from ARTICULO ORDER BY ID DESC";
            con = DB.getInstancia().getConexion(); //referencia a la conexion.
            PreparedStatement prepareStatement = con.prepareStatement(query);
            ResultSet rs = prepareStatement.executeQuery();
            while(rs.next()){
                Articulo articulo = new Articulo();
                long ID = rs.getLong("id");
                articulo.setId(ID);
                articulo.setAutor(UsuarioServices.getUsuario(rs.getLong("autorid") ) );
                articulo.setComentarios(ComentarioServices.getComentarioByArticuloID(ID));
                articulo.setEtiquetas(EtiquetaServices.getEtiquetaByArticuloID(ID));
                articulo.setCuerpo(rs.getString("cuerpo"));
                articulo.setFecha(rs.getDate("fecha"));
                articulo.setTitulo(rs.getString("titulo"));
                lista.add(articulo);
            }

        } catch (SQLException ex) {
            Logger.getLogger(ArticuloServices.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(ArticuloServices.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return lista;
    }

    public static Articulo getArticulo(long id) {
        Articulo articulo = null;
        Connection con = null;
        try {
            //utilizando los comodines (?)...
            String query = "select * from Articulo where id = ?";
            con = DB.getInstancia().getConexion();
            PreparedStatement prepareStatement = con.prepareStatement(query);
            //Antes de ejecutar seteo los parametros.
            prepareStatement.setLong(1, id);
            //Ejecuto...
            ResultSet rs = prepareStatement.executeQuery();
            while(rs.next()){
                articulo = new Articulo();
                long ID = rs.getLong("id");
                articulo.setId(ID);
                articulo.setAutor(UsuarioServices.getUsuario(rs.getLong("autorid") ) );
                articulo.setComentarios(ComentarioServices.getComentarioByArticuloID(ID));
                articulo.setEtiquetas(EtiquetaServices.getEtiquetaByArticuloID(ID));
                articulo.setCuerpo(rs.getString("cuerpo"));
                articulo.setFecha(rs.getDate("fecha"));
                articulo.setTitulo(rs.getString("titulo"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(ArticuloServices.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(ArticuloServices.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return articulo;
    }

    public boolean crearArticulo(String titulo, String cuerpo, long autorID){
        boolean ok =false;
        Connection con = null;
        try {
            String query = "insert into ARTICULO(autorid,TITULO, CUERPO) values(?,?,?)";
            con = DB.getInstancia().getConexion();
            PreparedStatement prepareStatement = con.prepareStatement(query);
            //Antes de ejecutar seteo los parametros
            prepareStatement.setLong(1, autorID);
            prepareStatement.setString(2, titulo);
            prepareStatement.setString(3, cuerpo);

            int fila = prepareStatement.executeUpdate();
            ok = fila > 0 ;

        } catch (SQLException ex) {
            Logger.getLogger(ArticuloServices.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(ArticuloServices.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return ok;
    }
/*
    public boolean actualizarArticulo(Articulo articulo){
        boolean ok =false;

        Connection con = null;
        try {

            String query = "update Articulo set ARTICULO=?, USERNAME=?, PASSWORD=?, ADMINISTRADOR=?, AUTOR=? where id = ?";
            con = DB.getInstancia().getConexion();
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);
            //Antes de ejecutar seteo los parametros.
            prepareStatement.setString(1, articulo.getNombre());
            prepareStatement.setString(2, articulo.getUsername());
            prepareStatement.setString(3, articulo.getPassword());
            prepareStatement.setBoolean(4, articulo.isAdministrador());
            prepareStatement.setBoolean(5, articulo.isAutor());
            //Indica el where...
            prepareStatement.setLong(6, articulo.getId());
            //
            int fila = prepareStatement.executeUpdate();
            ok = fila > 0 ;

        } catch (SQLException ex) {
            Logger.getLogger(ArticuloServices.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(ArticuloServices.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return ok;
    }

    public boolean borrarArticulo(int id){
        boolean ok =false;

        Connection con = null;
        try {

            String query = "delete from Articulo where id = ?";
            con = DB.getInstancia().getConexion();
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);

            //Indica el where...
            prepareStatement.setInt(1, id);
            //
            int fila = prepareStatement.executeUpdate();
            ok = fila > 0 ;

        } catch (SQLException ex) {
            Logger.getLogger(ArticuloServices.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(ArticuloServices.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return ok;
    }*/

}
