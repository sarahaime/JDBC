package servicios;

import modelos.Usuario;
import org.jasypt.util.password.StrongPasswordEncryptor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UsuarioServices {

    public List<Usuario> listaUsuarios() {
        List<Usuario> lista = new ArrayList<>();
        Connection con = null; //objeto conexion.
        try {

            String query = "select * from USUARIO";
            con = DB.getInstancia().getConexion(); //referencia a la conexion.
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);
            ResultSet rs = prepareStatement.executeQuery();
            while(rs.next()){
                Usuario usuario = new Usuario();
                usuario.setId(rs.getLong("id"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setUsername(rs.getString("username"));
                usuario.setPassword(rs.getString("password"));
                usuario.setAdministrador(rs.getBoolean("administrador"));
                usuario.setAutor(rs.getBoolean("autor"));

                lista.add(usuario);
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

    public static Usuario getUsuario(long id) {
        Usuario usuario = null;
        Connection con = null;
        try {
            //utilizando los comodines (?)...
            String query = "select * from Usuario where id = ?";
            con = DB.getInstancia().getConexion();
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);
            //Antes de ejecutar seteo los parametros.
            prepareStatement.setLong(1, id);
            //Ejecuto...
            ResultSet rs = prepareStatement.executeQuery();
            while(rs.next()){
                usuario = new Usuario();
                usuario.setId(rs.getLong("id"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setUsername(rs.getString("username"));
                usuario.setPassword(rs.getString("password"));
                usuario.setAdministrador(rs.getBoolean("administrador"));
                usuario.setAutor(rs.getBoolean("autor"));
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

        return usuario;
    }

    public Usuario getUsuarioByUserName(String username) {
        Usuario usuario = null;
        Connection con = null;
        try {
            //utilizando los comodines (?)...
            String query = "select * from Usuario where USERNAME = ?";
            con = DB.getInstancia().getConexion();
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);
            //Antes de ejecutar seteo los parametros.
            prepareStatement.setString(1, username);
            //Ejecuto...
            ResultSet rs = prepareStatement.executeQuery();
            while(rs.next()){
                usuario = new Usuario();
                usuario.setId(rs.getLong("id"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setUsername(rs.getString("username"));
                usuario.setPassword(rs.getString("password"));
                usuario.setAdministrador(rs.getBoolean("administrador"));
                usuario.setAutor(rs.getBoolean("autor"));
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

        return usuario;
    }

    public boolean crearUsuario(Usuario usuario){
        boolean ok =false;
        //para guardar la contraseña cifrada
        StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
        String encryptedPassword = passwordEncryptor.encryptPassword(usuario.getPassword());

        Connection con = null;
        try {

            String query = "insert into Usuario(NOMBRE, USERNAME, PASSWORD, ADMINISTRADOR, AUTOR) values(?,?,?,?,?)";
            con = DB.getInstancia().getConexion();
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);
            //Antes de ejecutar seteo los parametros
            prepareStatement.setString(1, usuario.getNombre());
            prepareStatement.setString(2, usuario.getUsername());
            prepareStatement.setString(3, encryptedPassword);
            prepareStatement.setBoolean(4, usuario.isAdministrador());
            prepareStatement.setBoolean(5, usuario.isAutor());
            //
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

    public boolean actualizarUsuario(Usuario usuario){
        boolean ok =false;

        Connection con = null;
        try {

            String query = "update Usuario set nombre=?, USERNAME=?, PASSWORD=?, ADMINISTRADOR=?, AUTOR=? where id = ?";
            con = DB.getInstancia().getConexion();
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);
            //Antes de ejecutar seteo los parametros.
            prepareStatement.setString(1, usuario.getNombre());
            prepareStatement.setString(2, usuario.getUsername());
            prepareStatement.setString(3, usuario.getPassword());
            prepareStatement.setBoolean(4, usuario.isAdministrador());
            prepareStatement.setBoolean(5, usuario.isAutor());
            //Indica el where...
            prepareStatement.setLong(6, usuario.getId());
            //
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

    public boolean borrarUsuario(int id){
        boolean ok =false;

        Connection con = null;
        try {

            String query = "delete from Usuario where id = ?";
            con = DB.getInstancia().getConexion();
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);

            //Indica el where...
            prepareStatement.setInt(1, id);
            //
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

    public Usuario autenticarUsuario(String userName, String pass ){
        Usuario usuario = getUsuarioByUserName(userName);

        if( usuario == null ){ return  null; }

        System.out.println(usuario.getPassword());

        StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
        if (passwordEncryptor.checkPassword(pass, usuario.getPassword())) {
            return usuario;
        } else {
            return null;
        }
    }

}
