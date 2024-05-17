
package Controlador;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author litio
 */
public class ControladorProducto {
    
    public void mostrarProductos(JTable tablaTotalProductos) {

        Configuracion.CConexion objetoConexion = new Configuracion.CConexion();

        Modelos.ModeloProducto objetoProducto = new Modelos.ModeloProducto();

        DefaultTableModel modelo = new DefaultTableModel();

        String sql = "";

        modelo.addColumn("id");
        modelo.addColumn("NombreProd");
        modelo.addColumn("Precio");
        modelo.addColumn("Stock");

        tablaTotalProductos.setModel(modelo);

        sql = "select producto.idproducto, producto.nombre, producto.precioProducto, producto.stock from producto;";

        try {
            Statement st = objetoConexion.estableceConexion().createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                objetoProducto.setIdProducto(rs.getInt("idproducto"));
                objetoProducto.setNombreProducto(rs.getString("nombre"));
                objetoProducto.setPrecioProducto(rs.getInt("precioProducto"));
                objetoProducto.setStockProducto(rs.getInt("stock"));

                modelo.addRow(new Object[]{objetoProducto.getIdProducto(), objetoProducto.getNombreProducto(), objetoProducto.getPrecioProducto(), objetoProducto.getStockProducto()});

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al mostrar productos " + e.toString());
        } finally {

            objetoConexion.cerrarConexion();
        }
    }
    
    public void agregarProducto(JTextField nombres, JTextField precioProducto, JTextField stock) {

        Configuracion.CConexion objetoConexion = new Configuracion.CConexion();
        Modelos.ModeloProducto objetoProducto = new Modelos.ModeloProducto();

        String consulta = "insert into producto (nombre, precioProducto, stock) values (?,?,?);";

        try {

            objetoProducto.setNombreProducto(nombres.getText());
            objetoProducto.setPrecioProducto(Integer.parseInt(precioProducto.getText()));
            objetoProducto.setStockProducto(Integer.parseInt(stock.getText()));

            CallableStatement cs = objetoConexion.estableceConexion().prepareCall(consulta);
            cs.setString(1, objetoProducto.getNombreProducto());
            cs.setInt(2, objetoProducto.getPrecioProducto());
            cs.setInt(3, objetoProducto.getStockProducto());

            cs.execute();

            JOptionPane.showMessageDialog(null, "Se guardó correctamente");

        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, "Error al guardar " + e.toString());
        } finally {

            objetoConexion.cerrarConexion();
        }

    }
    
    public void selecionar(JTable totalProducto, JTextField id, JTextField nombres,JTextField precioProducto, JTextField stock) {
    
        int fila = totalProducto.getSelectedRow();
        try {
            if (fila>=0) {
                
                id.setText(totalProducto.getValueAt(fila, 0).toString());
                nombres.setText(totalProducto.getValueAt(fila, 1).toString());
                precioProducto.setText(totalProducto.getValueAt(fila, 2).toString());
                stock.setText(totalProducto.getValueAt(fila, 3).toString());
                
            }
        } catch (Exception e) {
            
            JOptionPane.showMessageDialog(null, "Error al seleccionar: " + e.toString());
        }
    }
    
    public void modificarProducto (JTextField id, JTextField nombres, JTextField precioProducto, JTextField stock){
         
        Configuracion.CConexion objetoConexion = new Configuracion.CConexion();
        Modelos.ModeloProducto objetoProducto = new Modelos.ModeloProducto();
        
        String consulta = "update producto set producto.nombre=?, producto.precioProducto=?, producto.stock=? where producto.idproducto=?;";
        
        try {
            
            objetoProducto.setIdProducto(Integer.parseInt(id.getText()));
            objetoProducto.setNombreProducto(nombres.getText());
            objetoProducto.setPrecioProducto(Integer.parseInt(precioProducto.getText()));
            objetoProducto.setStockProducto(Integer.parseInt(stock.getText()));
            
            CallableStatement cs = objetoConexion.estableceConexion().prepareCall(consulta);
            
            cs.setString(1, objetoProducto.getNombreProducto());
            cs.setInt(2, objetoProducto.getPrecioProducto());
            cs.setInt(3, objetoProducto.getStockProducto());
            cs.setInt(4, objetoProducto.getIdProducto());
            
            cs.execute();
            
            JOptionPane.showMessageDialog(null, "Se modifico correctamente");
            
        } catch (Exception e) {
            
            JOptionPane.showMessageDialog(null, "Error al seleccionar: " + e.toString());
        } finally {
            objetoConexion.cerrarConexion();
        }
    }
    
    public void limpiarCamposProductos(JTextField id, JTextField nombres, JTextField precioProducto, JTextField stock){
        
        id.setText("");
        nombres.setText("");
        precioProducto.setText("");
        stock.setText("");
    }
    
    public void eliminarProducto (JTextField id){
        
        Configuracion.CConexion objetoConexion = new Configuracion.CConexion();
        Modelos.ModeloProducto objetoProducto = new Modelos.ModeloProducto();
        
        String consulta ="delete from producto where producto.idproducto=?;";
        
        try {
            
            objetoProducto.setIdProducto(Integer.parseInt(id.getText()));
            
            CallableStatement cs = objetoConexion.estableceConexion().prepareCall(consulta);
            
            cs.setInt(1, objetoProducto.getIdProducto());
            
            cs.execute();
            
            JOptionPane.showMessageDialog(null, "Se elimino correctamente");
            
        } catch (Exception e) {
            
            JOptionPane.showMessageDialog(null, "No se logro eliminar: " + e.toString());
            
        } finally {
            
            objetoConexion.cerrarConexion();
        }
    }
}
