/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import static aplicacion.Main.setRoot;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author david
 */
public class PantallaPrincipalUsuarioController implements Initializable {

    @FXML
    private Label etiquetaAlias;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Esto se ejecuta al iniciar
        modelo.secretario.setTitulo("Menú principal");
        etiquetaAlias.setText(modelo.secretario.getUsuario().getNickName());
    }
    /**
     * Para el initialize: - Recupera el objeto sesion y el objeto usuario del
     * modelo
     */
    
    
    /**
     * Envia los datos del usuario actual a la vista de registro y crea la
     * ventana modal del registro
     */
    public void editarPerfil(ActionEvent e) {}

    
    /**
     * Cambia a la ventana de seleccion de ejercicio
     */
    public void realizarProblema(ActionEvent e) {}

    
    /**
     * Envia los datos del usuario actual a la vista de ver resultados cambia la
     * vista a la de ver resultados
     */
    public void verResultados(ActionEvent e) {}

    


    
    /**
     * Enseña el mapa? seguro?
     */
    public void ensenaMapa(ActionEvent e) {}

    
    /**
     * Crea un dialogo modal confirmando que se quiere cerrar sesion y cambia la
     * vista a la inicio de sesion
     */
    @FXML
    private void pulsarCerrarSesion(ActionEvent event) throws IOException {
        modelo.secretario.cerrarSesion();
        setRoot("inicioSesion");
    }

}
