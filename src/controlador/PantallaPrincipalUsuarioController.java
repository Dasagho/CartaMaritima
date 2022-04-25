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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

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

    
    /**
     * Cambia a la ventana de seleccion de ejercicio
     */
    @FXML
    private void pulsarRealizarProblema(ActionEvent event) throws IOException {
        setRoot("seleccionTipoPregunta");
    }
    
    
    /**
     * Envia los datos del usuario actual a la vista de ver resultados cambia la
     * vista a la de ver resultados
     */
    @FXML
    private void pulsarVerResultados(ActionEvent event) {
    }
    
    
    /**
     * Envia los datos del usuario actual a la vista de registro y crea la
     * ventana modal del registro
     */
    @FXML
    private void pulsarEditarPerfil(ActionEvent event) throws IOException {
        
        
        FXMLLoader miCargador = new FXMLLoader(getClass().getResource("/vista/resgistro.fxml"));
        Parent root = miCargador.load();
        
        registrarseController controladorRegistro = miCargador.getController();
        
        controladorRegistro.initEdicion();
        
        
        Scene escena = new Scene(root, 600, 400);
        Stage escenario = new Stage();
        escenario.setScene(escena);
     //   escenario.setTitle("Editando tu perfil");
        escenario.initModality(Modality.APPLICATION_MODAL); // Hacemos que la ventana nueva sea modal
        escenario.showAndWait();
    
        
    }
        


}
