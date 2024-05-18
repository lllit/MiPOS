
package Controlador;

import Modelos.ModeloCliente;
import com.mysql.cj.xdevapi.PreparableStatement;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author litio
 */
public class ControladorVenta {
    
    public void buscarProducto(JTextField nombreProducto, JTable tablaProductos){
    
        Configuracion.CConexion objetoConexion = new Configuracion.CConexion();
        Modelos.ModeloProducto objetoProducto = new Modelos.ModeloProducto();
        
        
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("id");
        modelo.addColumn("NombreP");
        modelo.addColumn("PrecioProducto");
        modelo.addColumn("Stock");
        
        tablaProductos.setModel(modelo);
        
        try {
            String consulta="select * from producto where producto.nombre like concat('%',?,'%');";
            PreparedStatement ps = objetoConexion.estableceConexion().prepareStatement(consulta);
            
            ps.setString(1, nombreProducto.getText());
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {                
                
                objetoProducto.setIdProducto(rs.getInt("idproducto"));
                objetoProducto.setNombreProducto(rs.getString("nombre"));
                objetoProducto.setPrecioProducto(rs.getInt("precioProducto"));
                objetoProducto.setStockProducto(rs.getInt("stock"));
                
                modelo.addRow(new Object[]{objetoProducto.getIdProducto(),objetoProducto.getNombreProducto(),objetoProducto.getPrecioProducto(),objetoProducto.getStockProducto()});
            }
            tablaProductos.setModel(modelo);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al mostrar: "+e.toString());
        } finally {
            objetoConexion.cerrarConexion();
        }
        
        for (int column = 0; column < tablaProductos.getColumnCount(); column++) {
            
            Class <?> columClass = tablaProductos.getColumnClass(column);
            tablaProductos.setDefaultEditor(columClass, null);
        }
    }
    
    public void seleccionarProductosVenta (JTable tablaProducto, JTextField id,JTextField nombres,JTextField precioProducto, JTextField stock, JTextField precioFinal){
        
        int fila = tablaProducto.getSelectedRow();
        
        try {
            if (fila>=0) {
                
                id.setText(tablaProducto.getValueAt(fila, 0).toString());
                nombres.setText(tablaProducto.getValueAt(fila, 1).toString());
                precioProducto.setText(tablaProducto.getValueAt(fila, 2).toString());
                stock.setText(tablaProducto.getValueAt(fila, 3).toString());
                precioFinal.setText(tablaProducto.getValueAt(fila, 2).toString());
            }
        } catch (Exception e) {
            
            JOptionPane.showMessageDialog(null, "Error de seleccion: "+e.toString());
        }
    }
    
    public void buscarCliente(JTextField nombreCliente, JTable tablaCliente){
    
        Configuracion.CConexion objetoConexion = new Configuracion.CConexion();
        Modelos.ModeloCliente objetoCliente = new Modelos.ModeloCliente();
        
        
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("id");
        modelo.addColumn("Nombre");
        modelo.addColumn("ApPaterno");
        modelo.addColumn("ApMaterno");
        
        tablaCliente.setModel(modelo);
        
        try {
            String consulta="select * from cliente where cliente.nombres like concat('%',?,'%');";
            PreparedStatement ps = objetoConexion.estableceConexion().prepareStatement(consulta);
            
            ps.setString(1, nombreCliente.getText());
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {                
                
                objetoCliente.setIdCliente(rs.getInt("idcliente"));
                objetoCliente.setNombres(rs.getString("nombres"));
                objetoCliente.setApPaterno(rs.getString("appaterno"));
                objetoCliente.setApMaterno(rs.getString("apmaterno"));
                
                modelo.addRow(new Object[]{objetoCliente.getIdCliente(),objetoCliente.getNombres(),objetoCliente.getApPaterno(),objetoCliente.getApMaterno()});
            }
            tablaCliente.setModel(modelo);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al mostrar: "+e.toString());
        } finally {
            objetoConexion.cerrarConexion();
        }
        
        for (int column = 0; column < tablaCliente.getColumnCount(); column++) {
            
            Class <?> columClass = tablaCliente.getColumnClass(column);
            tablaCliente.setDefaultEditor(columClass, null);
        }
    }
    
    public void seleccionarClienteVenta (JTable tablaCliente, JTextField id,JTextField nombres,JTextField appaterno, JTextField apmaterno){
        
        int fila = tablaCliente.getSelectedRow();
        
        try {
            if (fila>=0) {
                
                id.setText(tablaCliente.getValueAt(fila, 0).toString());
                nombres.setText(tablaCliente.getValueAt(fila, 1).toString());
                appaterno.setText(tablaCliente.getValueAt(fila, 2).toString());
                apmaterno.setText(tablaCliente.getValueAt(fila, 3).toString());
            }
        } catch (Exception e) {
            
            JOptionPane.showMessageDialog(null, "Error de seleccion: "+e.toString());
        }
    }
    
    public void pasarProductosVentas(JTable tablaResumen, JTextField idproducto, JTextField nombreproducto, JTextField precioProducto, JTextField cantidadVenta, JTextField stock){
        
        DefaultTableModel modelo = (DefaultTableModel) tablaResumen.getModel();
        
        int stockDisponible = Integer.parseInt(stock.getText());
        
        String idProducto = idproducto.getText();
        
        for (int i = 0; i < modelo.getRowCount(); i++) {
            String idExistente = (String) modelo.getValueAt(i, 0);
            
            if (idExistente.equals(idProducto)) {
                JOptionPane.showMessageDialog(null, "Producto ya esta registrado");
                return;
            }
        }
        
        String nProducto = nombreproducto.getText();
        int precioUnitario = Integer.parseInt(precioProducto.getText());
        int cantidad = Integer.parseInt(cantidadVenta.getText());
        
        if (cantidad>stockDisponible) {
            
            JOptionPane.showMessageDialog(null, "La cantidad de venta no puede ser mayor al stock disponible");
            
            return;
        }
        
        int subtotal = precioUnitario * cantidad;
        
        modelo.addRow(new Object[]{idProducto,nProducto,precioUnitario,cantidad,subtotal});
        
    }
    
    public void eliminarProductosSeleccionadoResumenVenta(JTable tablaResumen) {
        
        try {
            DefaultTableModel modelo = (DefaultTableModel) tablaResumen.getModel();

        int indiceSeleccionado = tablaResumen.getSelectedRow();

        if (indiceSeleccionado != -1) {

            modelo.removeRow(indiceSeleccionado);
        } 
        else {
        JOptionPane.showMessageDialog(null, "Seleccione una fila para eliminar");
        }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al seleccionar"+e.toString());
        }
    }
    
    public void calcularTotalPagar(JTable tablaResumen, JLabel IVA, JLabel totalPagar){
        
        DefaultTableModel modelo = (DefaultTableModel) tablaResumen.getModel();
        
        int totalSubTotal = 0;
        double iva = 0.19;
        int totalIva=0;
        
        for (int i = 0; i < modelo.getRowCount(); i++) {
            
            totalSubTotal = totalSubTotal+(int)modelo.getValueAt(i, 4);
            totalIva= (int)(iva*totalSubTotal);
        }
        
        totalPagar.setText(String.valueOf(totalSubTotal));
        IVA.setText(String.valueOf(totalIva));
    }
    
    public void crearFactura(JTextField codCliente){
        
        Configuracion.CConexion objetoConexion = new Configuracion.CConexion();
        Modelos.ModeloCliente objetoCliente = new Modelos.ModeloCliente();
        
        String consulta ="insert into factura (fechaFactura, fkcliente) values (curdate(),?);";
        
        try {
            objetoCliente.setIdCliente(Integer.parseInt(codCliente.getText()));
            
            CallableStatement cs = objetoConexion.estableceConexion().prepareCall(consulta);
            cs.setInt(1, objetoCliente.getIdCliente());
            
            cs.execute();
            
            JOptionPane.showMessageDialog(null, "Factura Creada");
            
        } catch (Exception e) {
            
            JOptionPane.showMessageDialog(null, "Error al crear factura"+e.toString());
            
        } finally {
            
            objetoConexion.cerrarConexion();
        }
    }
    
    public void realizarVenta(JTable tablaResumenVenta){
    
        
        Configuracion.CConexion objetoConexion = new Configuracion.CConexion();
        
        String consultaDetalle = "insert into detalle (fkfactura,fkproducto,cantidad,precioVenta) values ((select max(idfactura) from factura),?,?,?);";
        String consultaStock="update producto set producto.stock = stock - ? where idproducto= ?;";
        
        try {
            
            PreparedStatement psDetalle = objetoConexion.estableceConexion().prepareStatement(consultaDetalle);
            PreparedStatement psStock = objetoConexion.estableceConexion().prepareStatement(consultaStock);
            
            int filas = tablaResumenVenta.getRowCount();
            
            for (int i = 0; i < filas; i++) {
                
                int idProducto = Integer.parseInt(tablaResumenVenta.getValueAt(i, 0).toString());
                int cantidad = Integer.parseInt(tablaResumenVenta.getValueAt(i, 3).toString());
                int precioVenta = Integer.parseInt(tablaResumenVenta.getValueAt(i, 2).toString());
                
                psDetalle.setInt(1, idProducto);
                psDetalle.setInt(2, cantidad);
                psDetalle.setInt(3, precioVenta);
                psDetalle.executeUpdate();
                
                
                psStock.setInt(1, cantidad);
                psStock.setInt(2, idProducto);
                
                psStock.executeUpdate();
            }
            
            JOptionPane.showMessageDialog(null, "Venta realizada");
            
        } catch (Exception e) {
            
            JOptionPane.showMessageDialog(null, "Error al vender "+e.toString());
            
        } finally {
            
            objetoConexion.cerrarConexion();
        }
    }
    
    public void limpiarCamposLuegoVenta(JTextField buscarCliente, JTable tablaCliente, JTextField buscarProducto, JTable tablaProducto, 
                                        JTextField selectIdCliente, JTextField selectNombreCliente, JTextField selectApPaternoCliente, 
                                        JTextField selectApMaternoCliente, JTextField selectIdProducto, JTextField selectNombreProducto, 
                                        JTextField selectPrecioProducto, JTextField selectStockProducto, JTextField precioVenta, JTextField cantidadVenta, 
                                        JTable tablaResumen, JLabel IVA, JLabel total){
    
        
        buscarCliente.setText("");
        buscarCliente.requestFocus();
        DefaultTableModel modeloCliente = (DefaultTableModel) tablaCliente.getModel();
        modeloCliente.setRowCount(0);
        
        buscarProducto.setText("");
        DefaultTableModel modeloProducto = (DefaultTableModel) tablaProducto.getModel();
        modeloProducto.setRowCount(0);
    
        selectIdCliente.setText("");
        selectNombreCliente.setText("");
        selectApPaternoCliente.setText("");
        selectApMaternoCliente.setText("");

        selectIdProducto.setText("");
        selectNombreProducto.setText("");
        selectPrecioProducto.setText("");
        selectStockProducto.setText("");
        
        precioVenta.setText("");
        precioVenta.setEnabled(false);
        
        cantidadVenta.setText("");
        
        DefaultTableModel modeloResumenVenta = (DefaultTableModel) tablaResumen.getModel();
        modeloResumenVenta.setRowCount(0);
        
        IVA.setText("----");
        total.setText("----");
    }
        
        
    public void mostrarUltimaFactura(JLabel ultimaFactura){
        
        Configuracion.CConexion objetoConexion = new Configuracion.CConexion();
        
        try {
            
            String consulta="select max(idfactura) as ultimaFactura from factura;";
            
            PreparedStatement ps = objetoConexion.estableceConexion().prepareCall(consulta);
            
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                ultimaFactura.setText(String.valueOf(rs.getInt("ultimaFactura")));
                
            }
        } catch (Exception e) {
            
            JOptionPane.showMessageDialog(null, "Error al mostrar la ultima factura "+e.toString());
        } finally {
            
            objetoConexion.cerrarConexion();
        }
    }    
}
