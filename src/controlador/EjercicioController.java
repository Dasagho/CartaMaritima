/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleGroup;

/**
 * FXML Controller class
 *
 * @author david
 */
public class EjercicioController implements Initializable {

    @FXML
    private ToggleGroup respuestas;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    /**
     * Dialogo modal confirmando que quiere salir de la pregunta cambia la vista
     * a la pantalla principal de usuario
     */
    
    public void cancelar(ActionEvent e) {
    }

    
    /**
     * Dialogo modal confirmando que quiere enviar respuesta de la pregunta
     * Cambia el color de la respuesta correcta a verde y si tu respuesta es la
     * erronea se marca en verde Ademas se muestra un letrero con un mensaje
     * indicando si has acertado o si no Muesta el boton de volver a pantalla
     * principal y deshabilita el boton confirmar
     */
    
    public void confirmar(ActionEvent e) {
    }
    

    /**
     * Cambia la ventana a la de principal usuario
     */
    
    public void volver(ActionEvent e) {
    }

}
