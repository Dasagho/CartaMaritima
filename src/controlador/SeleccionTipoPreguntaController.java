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
import javafx.scene.control.ToggleGroup;

/**
 * FXML Controller class
 *
 * @author david
 */
public class SeleccionTipoPreguntaController implements Initializable {

    @FXML
    private ToggleGroup sel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void pulsarCancelar(ActionEvent event) throws IOException {
        setRoot("PaginaPrincipalUsuario");
    }

    @FXML
    private void pulsarAceptar(ActionEvent event) {
    }

}
