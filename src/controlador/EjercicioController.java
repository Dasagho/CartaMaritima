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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
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
    @FXML
    private HBox hBoxBotones;
    @FXML
    private Region regionBotones;
    @FXML
    private VBox vBoxMapa;

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

            if (respuestaCorrecta) {
                modelo.secretario.sumarAcierto();
            } else {
                modelo.secretario.sumarFallo();
            }

            mostrarResultados(respuestaCorrecta, seleccionado);

            respuestaUsuario(respuestaCorrecta);
        } else {
            Alert mensaje= new Alert(Alert.AlertType.INFORMATION);
            mensaje.setTitle("Confirmar");
            mensaje.setHeaderText("No ha elegido respuesta");
            mensaje.setContentText("Por favor, elija una respuesta antes de confirmar");
            mensaje.showAndWait();
        }
    }

    private void respuestaUsuario(boolean respuestaCorrecta) {
        volver_Button.setText("Volver");
        volver_Button.setOnAction(this::volver);
        vBoxMapa.getChildren().clear();
        Text t = new Text();
        t.styleProperty().set("-fx-font-size:25px");
        t.setText(respuestaCorrecta ? "RESPUESTA CORRECTA,\n ¡ENHORABUENA!" : "RESPUESTA INCORRECTA,\n INTÉNTALO DE NUEVO");
        t.setFill(respuestaCorrecta ? Color.GREEN : Color.RED);
        vBoxMapa.getChildren().add(t);

    }

    @FXML
    private void pulsarMapa(ActionEvent event) throws IOException {
        if (!modelo.secretario.cartaAbierta()) {
            modelo.secretario.setCartaAbierta(true);
            FXMLLoader miCargador = new FXMLLoader(getClass().getResource("/vista/FXMLCartaNavegacion.fxml"));
            Parent root = miCargador.load();
            Scene escena = new Scene(root, 850, 550);
            Stage escenario = new Stage();
            escenario.setTitle("Carta de navegación");
            escenario.setMinHeight(750);
            escenario.setMinWidth(900);
            escenario.setScene(escena);
            escena.getStylesheets().add("/resources/estilos.css");
            escenario.show();

            escenario.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent e) {
                    modelo.secretario.setCartaAbierta(false);
                }
            });
        }
    }

    /**
     * Cambia la ventana a la de principal usuario
     */
    public void volver(ActionEvent e) {
        try {
            Main.setRoot("seleccionTipoPregunta");
        } catch (IOException ex) {
            Logger.getLogger(EjercicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void mostrarResultados(Boolean acertado, RadioButton radioButton) {
        hBoxBotones.getChildren().remove(confirmar_Button);
        hBoxBotones.getChildren().remove(regionBotones);
        RadioButton[] radiob = {resp1_radioButton, resp2_radioButton, resp3_radioButton, resp4_radioButton};
        radioButton.setTextFill(Paint.valueOf(acertado ? "green" : "red"));

        for (int i = 0; i < radiob.length; i++) {
            if (!radiob[i].isSelected() && ((Answer) respuestasLista.get(i)).getValidity()) {
                radiob[i].setTextFill(Paint.valueOf("green"));
            }
            radiob[i].setDisable(true);
            radiob[i].setOpacity(1);

        }
    }

}
