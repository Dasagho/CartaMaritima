/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import aplicacion.Main;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import model.Answer;
import model.Problem;

/**
 * FXML Controller class
 *
 * @author david
 */
public class EjercicioController implements Initializable {

    @FXML
    private ToggleGroup respuestas;
    @FXML
    private TextArea enunciado_textArea;
    @FXML
    private RadioButton resp1_radioButton;
    @FXML
    private RadioButton resp2_radioButton;
    @FXML
    private RadioButton resp3_radioButton;
    @FXML
    private RadioButton resp4_radioButton;
    @FXML
    private Button volver_Button;
    @FXML
    private Button confirmar_Button;

    private Problem problema;
    private List respuestasLista;
    private Object[] respuestaArray;

    /**
     * Initializes the controller class.
     */
    @Override
    @SuppressWarnings("empty-statement")
    public void initialize(URL url, ResourceBundle rb) {

        // Inicializacion del Singleton a traves del modelo secretario
        // modelo.secretario.initialize();
        // Nombramiento de la ventana
        modelo.secretario.setTitulo("Ejercicio");

        // Recuperacion del problema proporcionado por el secretario
        problema = modelo.secretario.getProblemas().get(modelo.secretario.getIndiceProblemaSel());

        enunciado_textArea.setText(problema.getText());
        respuestasLista = problema.getAnswers();

        // Tratamiento de las respuestas para pintarlas por pantalla
        respuestaArray = respuestasLista.toArray();
        RadioButton[] radiob = {resp1_radioButton, resp2_radioButton, resp3_radioButton, resp4_radioButton};

        for (int i = 0; i < respuestaArray.length; i++) {
            radiob[i].setText(((Answer) respuestaArray[i]).getText());
        }
    }

    /**
     * Dialogo modal confirmando que quiere salir de la pregunta cambia la vista
     * a la pantalla principal de usuario
     */
    @FXML
    public void cancelar(ActionEvent e) throws IOException {
        // Falta por implementar
        Main.setRoot("seleccionTipoPregunta");
    }

    /**
     * Dialogo modal confirmando que quiere enviar respuesta de la pregunta
     * Cambia el color de la respuesta correcta a verde y si tu respuesta es la
     * erronea se marca en verde Ademas se muestra un letrero con un mensaje
     * indicando si has acertado o si no Muesta el boton de volver a pantalla
     * principal y deshabilita el boton confirmar
     */
    @FXML
    public void confirmar(ActionEvent e) {
        RadioButton seleccionado = (RadioButton) resp1_radioButton.getToggleGroup().getSelectedToggle();

        if (seleccionado != null) {
            Boolean respuestaCorrecta = false;
            switch (seleccionado.getId()) {
                case "resp1_radioButton":
                    respuestaCorrecta = ((Answer) respuestasLista.get(0)).getValidity();
                    break;

                case "resp2_radioButton":
                    respuestaCorrecta = ((Answer) respuestasLista.get(1)).getValidity();
                    break;

                case "resp3_radioButton":
                    respuestaCorrecta = ((Answer) respuestasLista.get(2)).getValidity();
                    break;

                case "resp4_radioButton":
                    respuestaCorrecta = ((Answer) respuestasLista.get(3)).getValidity();
                    break;
            }

            if (respuestaCorrecta) { modelo.secretario.sumarAcierto(); } 
            else { modelo.secretario.sumarFallo(); }
            
            mostrarResultados(respuestaCorrecta, seleccionado);
            volver_Button.setText("volver");
            volver_Button.setOnAction(this::volver);
        }
    }

    @FXML
    private void mostrarCarta(ActionEvent event) throws IOException {
        FXMLLoader miCargador = new FXMLLoader(getClass().getResource("/vista/FXMLCartaNavegacion.fxml"));
        Parent root = miCargador.load();
        Scene escena = new Scene(root, 900, 600);
        Stage escenario = new Stage();
        escenario.setScene(escena);
        escena.getStylesheets().add("/resources/estilos.css");
        escenario.show();
    }

    /**
     * Cambia la ventana a la de principal usuario
     */
    public void volver(ActionEvent e) {
        try {
            Main.setRoot("PaginaPrincipalUsuario");
        } catch (IOException ex) {
            Logger.getLogger(EjercicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void mostrarResultados(Boolean acertado, RadioButton radioButton) {
        confirmar_Button.setDisable(true);
        RadioButton[] radiob = {resp1_radioButton, resp2_radioButton, resp3_radioButton, resp4_radioButton};
        radioButton.setTextFill(Paint.valueOf(acertado ? "green" : "red"));
        
        for (int i = 0; i < radiob.length; i++) {
            if (!radiob[i].isSelected() && ((Answer) respuestasLista.get(i)).getValidity()) { radiob[i].setTextFill(Paint.valueOf("green")); }
            radiob[i].setDisable(true);
            radiob[i].setOpacity(1);
            
        }
    }

}
