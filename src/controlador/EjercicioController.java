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
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
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

    private Problem problema;
    private List respuestasLista;

    /**
     * Initializes the controller class.
     */
    @Override
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
        Object[] r = respuestasLista.toArray();
        RadioButton[] radiob = {resp1_radioButton, resp2_radioButton, resp3_radioButton, resp4_radioButton};

        for (int i = 0; i < r.length; i++) {
            radiob[i].setText(((Answer) r[i]).getText());
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

            System.out.println(respuestaCorrecta ? "has acertado" : "has fallado");     // falta pulir
            
            if (respuestaCorrecta) modelo.secretario.sumarAcierto(); else modelo.secretario.sumarFallo();

            volver_Button.setText("volver");
            volver_Button.setOnAction(this::volver);
        }
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

}
