
package Controlador;

import com.toedter.calendar.JDateChooser;
import java.sql.Date;
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
public class controladorReportes {
    
    public void buscarMostrarDatosClientes (JTextField numeroFactura, JLabel numeroFacturaEncontrado, 
                                            JLabel fechaFacturaEncontrado, JLabel nombreCliente, 
                                            JLabel appaterno,JLabel apmaterno){
    
        
        Configuracion.CConexion objetoCConexion = new Configuracion.CConexion();
        
        try {
            
            String consulta="select factura.idfactura, factura.fechaFactura, cliente.nombres, cliente.appaterno,cliente.apmaterno from factura " +
                            "inner join cliente on cliente.idcliente = factura.fkcliente where factura.idfactura = ?;";
            
            PreparedStatement ps = objetoCConexion.estableceConexion().prepareStatement(consulta);
            ps.setInt(1, Integer.parseInt(numeroFactura.getText()));
            
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                
                numeroFacturaEncontrado.setText(String.valueOf(rs.getInt("idfactura")));
                fechaFacturaEncontrado.setText(rs.getDate("fechaFactura").toString());
                nombreCliente.setText(rs.getString("nombres"));
                appaterno.setText(rs.getString("appaterno"));
                apmaterno.setText(rs.getString("apmaterno"));
            }
            else
            {
                numeroFacturaEncontrado.setText("");
                fechaFacturaEncontrado.setText("");
                nombreCliente.setText("");
                appaterno.setText("");
                apmaterno.setText("");
                
                JOptionPane.showMessageDialog(null, "No se encontro la factura");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al encontrar la factura "+e.toString());
        } finally {
            objetoCConexion.cerrarConexion();
        }
    }
    
    public void buscarFacturaMostrarDatosProductos(JTextField numeroFactura, JTable tablaProducto, 
                                                   JLabel IVA, JLabel total){
                                                   
        Configuracion.CConexion objetoCConexion = new Configuracion.CConexion();
        
        DefaultTableModel modelo = new DefaultTableModel();
        
        modelo.addColumn("N.Producto");
        modelo.addColumn("Cantidad");
        modelo.addColumn("PrecioVenta");
        modelo.addColumn("SubTotal");
        
        tablaProducto.setModel(modelo);
        
        try {
            
            String consulta ="select producto.nombre, detalle.cantidad, detalle.precioVenta from detalle " +
                             "inner join factura on factura.idfactura = detalle.fkfactura " +
                             "inner join producto on producto.idproducto = detalle.fkproducto " +
                             "where factura.idfactura=?;";
            
            PreparedStatement ps = objetoCConexion.estableceConexion().prepareStatement(consulta);
            ps.setInt(1, Integer.parseInt(numeroFactura.getText()));
            
            ResultSet rs = ps.executeQuery();
            
            int totalFactura =0;
            double valorIVA = 0.19;
            
            
            while (rs.next()) {                
            
                String nombreProducto = rs.getString("nombre");
                int cantidad = rs.getInt("cantidad");
                int precioVenta = rs.getInt("precioVenta");
                int subTotal = cantidad*precioVenta;
                        
                totalFactura = (int)(totalFactura+subTotal);
                
                modelo.addRow(new Object[]{nombreProducto, cantidad,precioVenta,subTotal});
            }
            
            int totalIVA = (int)(totalFactura*valorIVA);
            
            IVA.setText(String.valueOf(totalIVA));
            
            total.setText(String.valueOf(totalFactura));
                    
        } catch (Exception e) {
            
            JOptionPane.showMessageDialog(null, "Error al mostrar los productos de la factura "+e.toString());
        } finally {
            objetoCConexion.cerrarConexion();
        }
    }
    
    public void mostrarTotalVentaPorFecha(JDateChooser desde, JDateChooser hasta, JTable tablaVentas,JLabel totalGeneral){
        
        Configuracion.CConexion objetoCConexion = new Configuracion.CConexion();
        
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("idFactura");
        modelo.addColumn("fechaFactura");
        modelo.addColumn("NProducto");
        modelo.addColumn("Cantidad");
        modelo.addColumn("PrecioVenta");
        modelo.addColumn("SubTotal");
        
        tablaVentas.setModel(modelo);
        
        try {
            
            String consulta ="select factura.idfactura, factura.fechaFactura, producto.nombre, detalle.cantidad, detalle.precioVenta from detalle " +
                             "inner join factura on factura.idfactura = detalle.fkfactura " +
                             "inner join producto on producto.idproducto = detalle.fkproducto " +
                             "where factura.fechaFactura Between ? and ?;";
            
            PreparedStatement ps = objetoCConexion.estableceConexion().prepareStatement(consulta);
            
            java.util.Date fechaDesde = desde.getDate();
            java.util.Date fechaHasta = hasta.getDate();
            
            java.sql.Date fechaDesdeSQL = new java.sql.Date(fechaDesde.getTime());
            java.sql.Date fechaHastaSQL = new java.sql.Date(fechaHasta.getTime());
            
            ps.setDate(1, fechaDesdeSQL);
            ps.setDate(2, fechaHastaSQL);
            
            ResultSet rs = ps.executeQuery();
            
            int totalFactura = 0;
            
            while (rs.next()) {
                
                int idFactura = rs.getInt("idfactura");
                Date fechaFactura = rs.getDate("fechaFactura");
                String nombreProducto = rs.getString("nombre");
                int cantidad = rs.getInt("cantidad");
                int precioVenta = rs.getInt("precioVenta");
                
                int subTotal = cantidad*precioVenta;
                
                totalFactura = (int)(totalFactura+subTotal);
                
                modelo.addRow(new Object[]{idFactura,fechaFactura,nombreProducto,cantidad,precioVenta,subTotal});
                
            }
            
            totalGeneral.setText(String.valueOf(totalFactura));
            
            
            
        } catch (Exception e) {
                
            JOptionPane.showMessageDialog(null, "Error al buscar los ingresos por fechas "+e.toString());
            
        } finally {
            objetoCConexion.cerrarConexion();
        }
        
        for (int column = 0; column < tablaVentas.getColumnCount(); column++) {
            
            Class<?> columnClass = tablaVentas.getColumnClass(column);
            tablaVentas.setDefaultEditor(columnClass, null);
        }
    }
}
