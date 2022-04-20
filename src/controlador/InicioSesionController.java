/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import DBAccess.NavegacionDAOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import model.Navegacion;
import model.User;

/**
 * FXML Controller class
 *
 * @author david
 */
public class InicioSesionController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicializacion del Singleton a traves del modelo secretario
        modelo.secretario.initialize();
    }

    /**
     * Para el iniatialize: cargar el controller de secretario
     */

    /*
     * 1º Chequea el correo electronico para verificar que es un correo electronico en formato valido y que existe en la base de datos
     * sino muestra un mensaje de error por el label errorCorreo
     * 
     * 2º Chequea que la contraseña no corresponde a este usuario
     *
     * 3º Carga el objeto usuario y creamos el objeto sesion y se lo pasa al modelo  
     *
     * 4º Cambia la ventana a la pantalla principal de usuario
     */
    @FXML
    public void iniciarSesion(ActionEvent e) {
    }

    /**
     * Muestra un cuadro de dialogo que pide que ingreses tu correo para que
     * pueda enviar mensaje de verificacion (no lo va a hacer) y se cierre el
     * cuadro
     */
    public void hasOlvidadoTuContrasena(ActionEvent e) {
    }

    /**
     * Te manda a la ventana de registro
     */
    public void todaviaNoTienesCuenta(ActionEvent e) {
    }

}
