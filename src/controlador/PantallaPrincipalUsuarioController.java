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
import javafx.event.ActionEvent;
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
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
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


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Esto se ejecuta al iniciar
        modelo.secretario.setTitulo("Menú principal");
        etiquetaAlias.setText(modelo.secretario.getUsuario().getNickName());
        imagenPerfil.setImage(modelo.secretario.getUsuario().getAvatar());
        
        // Resumen de historial
        List<Session> sesiones = modelo.secretario.getUsuario().getSessions();
        int problemasRealizados = 0;
        int aciertos = 0;
        Float porcentaje;
        for (int i = 0; i < sesiones.size(); i++){
            aciertos += sesiones.get(i).getHits();
            problemasRealizados += (aciertos + sesiones.get(i).getFaults());
        }
        xRealizados.setText("" + problemasRealizados);
        xAcertados.setText("" + aciertos);
        
        porcentaje = ((float) aciertos) / ((float) problemasRealizados);
        if (porcentaje.toString() != "NaN"){
            xPorcentaje.setText("" + porcentaje + "%");
        } else {xPorcentaje.setText("¿?");}
        
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
        if (respuesta.isPresent() && respuesta.get()==ButtonType.OK){
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
        setRoot("ResultadosUsuario");
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
        escenario.initModality(Modality.APPLICATION_MODAL); // Hacemos que la ventana nueva sea modal
        escenario.showAndWait();
    
        
    }

    @FXML
    private void pulsarMapa(MouseEvent event) throws IOException {
        FXMLLoader miCargador = new FXMLLoader(getClass().getResource("/vista/cartaNavegacion.fxml"));
        Parent root = miCargador.load();
        
        
        
        Scene escena = new Scene(root, 900, 600);
        Stage escenario = new Stage();
        escenario.setScene(escena);
     //   escenario.setTitle("Editando tu perfil");
     //   escenario.initModality(Modality.APPLICATION_MODAL); // Hacemos que la ventana nueva sea modal
        escenario.show();
    }

    @FXML
    private void pulsar(ActionEvent event) {
    }

        


}
