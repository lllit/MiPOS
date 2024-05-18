
package Configuracion;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

/**
 *
 * @author litio
 */
public class CConexion {
    
    
    Connection conectar = null;
    
    String usuario="root";
    String contraseña="Homero123";
    String bd="dbpos";
    String ip="localhost";
    String puerto="3306";
    
    String cadena ="jdbc:mysql://"+ip+":"+puerto+"/"+bd;
    
    public Connection estableceConexion(){
    
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conectar = DriverManager.getConnection(cadena, usuario,contraseña);
            //JOptionPane.showMessageDialog(null, "Conexion correcta a BD");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "No se conecto a la BD"+e.toString());
        }
    return conectar;
    }
    
    public void cerrarConexion(){
        
        try {
            if (conectar!=null && !conectar.isClosed()) {
                conectar.close();
                //JOptionPane.showMessageDialog(null, "Conexion cerrada");
            }
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, "No se logro cerrar la conexion"+e.toString());
        }
    }
}
