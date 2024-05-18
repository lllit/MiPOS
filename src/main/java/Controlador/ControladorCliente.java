package Controlador;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashSet;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author litio
 */
public class ControladorCliente {

    public void mostrarCliente(JTable tablaTotalClientes) {

        Configuracion.CConexion objetoConexion = new Configuracion.CConexion();

        Modelos.ModeloCliente objetoCliente = new Modelos.ModeloCliente();

        DefaultTableModel modelo = new DefaultTableModel();

        String sql = "";

        modelo.addColumn("id");
        modelo.addColumn("nombres");
        modelo.addColumn("appaterno");
        modelo.addColumn("apmaterno");

        tablaTotalClientes.setModel(modelo);

        sql = "select cliente.idcliente,cliente.nombres,cliente.appaterno,cliente.apmaterno from cliente";

        try {
            Statement st = objetoConexion.estableceConexion().createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                objetoCliente.setIdCliente(rs.getInt("idcliente"));
                objetoCliente.setNombres(rs.getString("nombres"));
                objetoCliente.setApPaterno(rs.getString("appaterno"));
                objetoCliente.setApMaterno(rs.getString("apmaterno"));

                modelo.addRow(new Object[]{objetoCliente.getIdCliente(), objetoCliente.getNombres(), objetoCliente.getApPaterno(), objetoCliente.getApMaterno()});

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al mostrar usuarios " + e.toString());
        } finally {

            objetoConexion.cerrarConexion();
        }
    }

    public void agregarCliente(JTextField nombres, JTextField appaterno, JTextField apmaterno) {

        Configuracion.CConexion objetoConexion = new Configuracion.CConexion();
        Modelos.ModeloCliente objetoCliente = new Modelos.ModeloCliente();

        String consulta = "insert into cliente (nombres,appaterno,apmaterno) values(?,?,?);";

        try {

            objetoCliente.setNombres(nombres.getText());
            objetoCliente.setApPaterno(appaterno.getText());
            objetoCliente.setApMaterno(apmaterno.getText());

            CallableStatement cs = objetoConexion.estableceConexion().prepareCall(consulta);
            cs.setString(1, objetoCliente.getNombres());
            cs.setString(2, objetoCliente.getApPaterno());
            cs.setString(3, objetoCliente.getApMaterno());

            cs.execute();

            JOptionPane.showMessageDialog(null, "Se guardó correctamente");

        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, "Error al guardar " + e.toString());
        } finally {

            objetoConexion.cerrarConexion();
        }

    }

    public void selecionar(JTable totalCliente, JTextField id, JTextField nombres, JTextField appaternno, JTextField apmaterno) {
    
        int fila = totalCliente.getSelectedRow();
        try {
            if (fila>=0) {
                
                id.setText(totalCliente.getValueAt(fila, 0).toString());
                nombres.setText(totalCliente.getValueAt(fila, 1).toString());
                appaternno.setText(totalCliente.getValueAt(fila, 2).toString());
                apmaterno.setText(totalCliente.getValueAt(fila, 3).toString());
                
            }
        } catch (Exception e) {
            
            JOptionPane.showMessageDialog(null, "Error al seleccionar: " + e.toString());
        }
    }
    
    public void modificarCliente (JTextField id, JTextField nombres, JTextField appaterno, JTextField apmaterno){
         
        Configuracion.CConexion objetoConexion = new Configuracion.CConexion();
        Modelos.ModeloCliente objetoCliente = new Modelos.ModeloCliente();
        
        String consulta = "update cliente set cliente.nombres=?, cliente.appaterno=?, cliente.apmaterno=? where cliente.idcliente=?;";
        
        try {
            
            objetoCliente.setIdCliente(Integer.parseInt(id.getText()));
            objetoCliente.setNombres(nombres.getText());
            objetoCliente.setApPaterno(appaterno.getText());
            objetoCliente.setApMaterno(apmaterno.getText());
            
            CallableStatement cs = objetoConexion.estableceConexion().prepareCall(consulta);
            
            cs.setString(1, objetoCliente.getNombres());
            cs.setString(2, objetoCliente.getApPaterno());
            cs.setString(3, objetoCliente.getApMaterno());
            cs.setInt(4, objetoCliente.getIdCliente());
            
            cs.execute();
            
            JOptionPane.showMessageDialog(null, "Se modifico correctamente");
            
        } catch (Exception e) {
            
            JOptionPane.showMessageDialog(null, "Error al seleccionar: " + e.toString());
        } finally {
            objetoConexion.cerrarConexion();
        }
    }
    
    public void limpiarCamposClientes(JTextField id, JTextField nombres, JTextField appaterno, JTextField apmaterno){
        
        id.setText("");
        nombres.setText("");
        appaterno.setText("");
        apmaterno.setText("");
    }
    
    public void eliminarClientes (JTextField id){
        
        Configuracion.CConexion objetoConexion = new Configuracion.CConexion();
        Modelos.ModeloCliente objetoCliente = new Modelos.ModeloCliente();
        
        String consulta ="delete from cliente where cliente.idcliente=?;";
        
        try {
            
            objetoCliente.setIdCliente(Integer.parseInt(id.getText()));
            
            CallableStatement cs = objetoConexion.estableceConexion().prepareCall(consulta);
            
            cs.setInt(1, objetoCliente.getIdCliente());
            
            cs.execute();
            
            JOptionPane.showMessageDialog(null, "Se elimino correctamente");
            
        } catch (Exception e) {
            
            JOptionPane.showMessageDialog(null, "No se logro eliminar: " + e.toString());
            
        } finally {
            
            objetoConexion.cerrarConexion();
        }
    }

}
