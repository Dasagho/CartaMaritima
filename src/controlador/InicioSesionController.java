/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import static aplicacion.Main.setRoot;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;

import model.User;

/**
 * FXML Controller class
 *
 * @author david
 */
public class InicioSesionController implements Initializable {

    @FXML
    private TextField email_textField;
    @FXML
    private PasswordField contrasena_textField;
    @FXML
    private Label email_error;
    @FXML
    private Label contrasena_error;
    @FXML
    private Button btn;
    @FXML
    private Label hasOlvidadoContrasena;
    @FXML
    private Label nombreUsuario_label;
    @FXML
    private Label contrasena_label;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Nombramiento de la ventana
        modelo.secretario.setTitulo("Iniciar sesión");

        // Inicializacion del Singleton a traves del modelo secretario
        modelo.secretario.initialize();

        email_textField.styleProperty().bind(Bindings.when(email_error.disableProperty())
                .then("")
                .otherwise("-fx-border-color: #F31226"));
        contrasena_textField.styleProperty().bind(Bindings.when(contrasena_error.disableProperty())
                .then("")
                .otherwise("-fx-border-color: #F31226"));

        email_textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            modelo.secretario.animacion(newVal, nombreUsuario_label);
            if (!newVal) {
                comprobarErrores();
            }
        });
        contrasena_textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            modelo.secretario.animacion(newVal, contrasena_label);
            if (!newVal) {
                comprobarErrores();
            }
        });
        email_textField.requestFocus();
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
    public void iniciarSesion(ActionEvent e) throws IOException {
        restablecerErrores();
        // AudioClip click = new AudioClip(getClass().getResource("/resources/Sounds/click.wav").toExternalForm());
        // click.play();

        /**
         * Al mostrar errores: ¿Mostrar todos los errores o de uno en uno?
         */
        if (!modelo.secretario.getNavegacion().exitsNickName(email_textField.getText())) {
            email_error.setDisable(false);
            email_error.setText("No existe un usuario con este NickName, introduce el NickName de un usuario registrado");
            email_textField.requestFocus(); // Pone el foco en el textField del nickName
            contrasena_textField.setText("");
            return;
        }

        User usuario = modelo.secretario.getNavegacion().loginUser(email_textField.getText(), contrasena_textField.getText());
        if (usuario == null) {
            contrasena_error.setDisable(false);
            contrasena_error.setText("La contraseña introducida no pertenece a este usuario, inténtalo de nuevo con otra contraseña");
            contrasena_textField.setText("");
            contrasena_textField.requestFocus();
            hasOlvidadoContrasena.setVisible(true);
            return;
        }

        modelo.secretario.setUsuario(usuario);
        modelo.secretario.iniciarSesion();
        setRoot("PaginaPrincipalUsuario");

    }

    /**
     * Muestra un cuadro de dialogo que pide que ingreses tu correo para que
     * pueda enviar mensaje de verificacion (no lo va a hacer) y se cierre el
     * cuadro
     */
    @FXML
    private void hasOlvidadoTuContrasena(MouseEvent event) {
        TextInputDialog dialogo = new TextInputDialog("");
        dialogo.setTitle("Recuperar contraseña");
        dialogo.setHeaderText("Introduce tu correo electrónico");
        Optional<String> recuperar = dialogo.showAndWait();
    }

    /**
     * Te manda a la ventana de registro
     */
    @FXML
    public void todaviaNoTienesCuenta(MouseEvent event) throws IOException {
        restablecerErrores();
        setRoot("resgistro");
    }

    public void comprobarErrores() {
        if (!modelo.secretario.getNavegacion().exitsNickName(email_textField.getText()) && !email_textField.getText().isEmpty()) {
            email_error.setDisable(false);
            email_error.setText("No existe un usuario con este NickName, introduce el NickName de un usuario registrado");
            contrasena_textField.setText("");
            return;
        } else {
            email_error.setText("");
            email_error.setDisable(true);
        }

        User usuario = modelo.secretario.getNavegacion().loginUser(email_textField.getText(), contrasena_textField.getText());
        if (!contrasena_textField.getText().isEmpty()) {
            contrasena_error.setText("");
            contrasena_error.setDisable(true);
        }
    }

    public void restablecerErrores() {
        email_error.setText("");
        email_error.setDisable(true);
        contrasena_error.setText("");
        contrasena_error.setDisable(true);
    }

}
