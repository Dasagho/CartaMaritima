/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import static aplicacion.Main.setRoot;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Session;

/**
 * FXML Controller class
 *
 * @author david
 */
public class PantallaPrincipalUsuarioController implements Initializable {

    @FXML
    private Label etiquetaAlias;
    @FXML
    private ImageView imagenPerfil;
    @FXML
    private Label xRealizados;
    @FXML
    private Label xAcertados;
    @FXML
    private Label xPorcentaje;
    @FXML
    private Button botonRealizarProblema;
    @FXML
    private MenuButton menuPerfil;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Esto se ejecuta al iniciar
        modelo.secretario.setTitulo("Menú principal");
        etiquetaAlias.setText(modelo.secretario.getUsuario().getNickName());
        imagenPerfil.setImage(modelo.secretario.getUsuario().getAvatar());

        // Resumen de historial
        int problemasRealizados = modelo.secretario.getTotalProblemasRealizados();
        int aciertos = modelo.secretario.getTotalAciertos();
        Float porcentaje;
        xRealizados.setText("" + problemasRealizados);
        xAcertados.setText("" + aciertos);
        porcentaje = ((float) ((int) ( ( ((float) aciertos) / ((float) problemasRealizados) ) * 10000))) /100;
        if (porcentaje.toString() != "NaN") {
            xPorcentaje.setText("" + porcentaje + "%");
        } else {
            xPorcentaje.setText("¿?");
        }

    }

    /**
     * Para el initialize: - Recupera el objeto sesion y el objeto usuario del
     * modelo
     */
    /**
     * Crea un dialogo modal confirmando que se quiere cerrar sesion y cambia la
     * vista a la inicio de sesion
     */
    @FXML
    private void pulsarCerrarSesion(ActionEvent event) throws IOException {

        Alert alerta = new Alert(AlertType.CONFIRMATION);
        alerta.setTitle("Cerrar sesión");
        alerta.setHeaderText("Cerrar sesión");
        alerta.setContentText("¿Seguro que quieres cerrar la sesión?\n ");
        Optional<ButtonType> respuesta = alerta.showAndWait();
        if (respuesta.isPresent() && respuesta.get() == ButtonType.OK) {
            // Se cierra la sesión
            modelo.secretario.cerrarSesion();
            setRoot("inicioSesion");
        }
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
    private void pulsarVerResultados(ActionEvent event) throws IOException {
        if (modelo.secretario.tieneSesiones()) {
            setRoot("ResultadosUsuario");
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Ver Resultados");
            alert.setHeaderText("No es posible ver resultados");
            alert.setContentText("Este usuario no tiene sesiones guardadas actualmente, así que no se pueden ver los resultados.");
            alert.showAndWait();
        }
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

        Scene escena = new Scene(root, 900, 750);
        escena.getStylesheets().add("/resources/estilos.css");
        Stage escenario = new Stage();
        escenario.setMinHeight(750);
        escenario.setMinWidth(900);
        escenario.setScene(escena);
        escenario.initModality(Modality.APPLICATION_MODAL); // Hacemos que la ventana nueva sea modal
        escenario.showAndWait();
        imagenPerfil.setImage(modelo.secretario.getUsuario().getAvatar()); // Actualiza la img de perfil por si ha sido modificada
    }

    @FXML
    private void pulsarMapa(ActionEvent event) throws IOException {
        if (!modelo.secretario.cartaAbierta()) {
            modelo.secretario.setCartaAbierta(true);
            FXMLLoader miCargador = new FXMLLoader(getClass().getResource("/vista/FXMLCartaNavegacion.fxml"));
            Parent root = miCargador.load();
            Scene escena = new Scene(root, 850, 550);
            Stage escenario = new Stage();
            escenario.setMinHeight(550);
            escenario.setMinWidth(850);
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

}
