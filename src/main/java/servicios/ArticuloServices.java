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

import static java.lang.Math.min;

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
                articulo.setAutor(UsuarioServices.getUsuario(rs.getLong("usuarioid") ) );
             //   System.out.println(articulo.getAutor().getUsername());
                articulo.setComentarios(ComentarioServices.getComentarioByArticuloID(ID));
                articulo.setEtiquetas(EtiquetaServices.getEtiquetaByArticuloID(ID));
                String cuerpo = rs.getString("cuerpo");
                //era 70 el limite pero con 200 se ve mejorsito
                articulo.setCuerpo( cuerpo.substring(0, min(200,cuerpo.length())) + "...");
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
                articulo.setAutor(UsuarioServices.getUsuario(rs.getLong("usuarioid") ) );
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

    public boolean crearArticulo(String titulo, String cuerpo, long usuarioid){
        boolean ok =false;
        Connection con = null;
        try {
            String query = "insert into ARTICULO(USUARIOID,TITULO, CUERPO) values(?,?,?)";
            con = DB.getInstancia().getConexion();
            PreparedStatement prepareStatement = con.prepareStatement(query);
            //Antes de ejecutar seteo los parametros
            prepareStatement.setLong(1, usuarioid);
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


    //Actualizar los Articulos.
    public boolean actualizarArticulo(Articulo articulo){
        boolean ok =false;

        Connection con = null;
        try {

            String query = "update Articulo set USUARIOID=?, TITULO=?, CUERPO=? where id = ?";
            con = DB.getInstancia().getConexion();
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);
            //Antes de ejecutar seteo los parametros.
            prepareStatement.setLong(1, articulo.getId());
            prepareStatement.setString(2, articulo.getTitulo());
            prepareStatement.setString(3, articulo.getCuerpo());

            //Indica el where...
            prepareStatement.setLong(4, articulo.getId());
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

    //Metodo borrar los Articulos
    public boolean borrarArticulo(long id, long usuarioid){
        boolean ok =false;

        Connection con = null;
        try {
            //que sea quien lo creo, o un administrador
            String query = "delete from Articulo where id = ? and (USUARIOID = ? or exists (select* from USUARIO where id = ? and administrador)) ";
            con = DB.getInstancia().getConexion();
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);

            //Indica el where...
            prepareStatement.setLong(1, id);
            prepareStatement.setLong(2, usuarioid);
            prepareStatement.setLong(3, usuarioid);
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



/*
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
