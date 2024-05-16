
package com.mycompany.mipos;

import Configuracion.CConexion;

/**
 *
 * @author MatiasPerez
 */
public class MiPOS {

    public static void main(String[] args) {
        /*Configuracion.CConexion objetoConexion = new Configuracion.CConexion();
        objetoConexion.estableceConexion();*/
        
        Formularios.MenuPrincipal objetoMenuPrincipal = new Formularios.MenuPrincipal();
        objetoMenuPrincipal.setVisible(true);
    }
}
