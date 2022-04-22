/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import DBAccess.NavegacionDAOException;
import aplicacion.Main;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import model.User;
import static model.User.checkNickName;
import static model.User.checkPassword;

/**
 * FXML Controller class
 *
 * @author david
 */
public class registrarseController implements Initializable {

    @FXML
    private Label nickName_error;
    @FXML
    private Label email_error;
    @FXML
    private Label contrasena_error;
    @FXML
    private Label confirmacion_error;
    @FXML
    private Label fecha_error;
    @FXML
    private TextField nickName_textfield;
    @FXML
    private TextField email_textfield;
    @FXML
    private PasswordField contrasena_textfield;
    @FXML
    private PasswordField confirmacion_textfield;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ImageView avatar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        // Nombramiento de la ventana
        modelo.secretario.setTitulo("Registrarse");

        // Asignacion de la fecha actual al datePicker para evitar errores de NullPointer
        datePicker.setValue(LocalDate.now());
    }

    @FXML
    private void cancelar(ActionEvent event) throws IOException {
        restablecerErrores();
        Main.setRoot("inicioSesion");
    }

    @FXML
    private void guardar(ActionEvent event) throws IOException {
        restablecerErrores();

        if (!checkNickName(nickName_textfield.getText())) {
            nickName_error.setText("Nombre de usuario no valido, un nombre valido debe tener entre 6 y 15 caracteres y solo puede contener mayúsculas, minúsculas, guiones o guiones bajos");
            nickName_textfield.requestFocus();
            return;
        }

        if (!User.checkEmail(email_textfield.getText())) {
            email_error.setText("Formato del correo electronico introducido no es valido por favor introduce un correo valido con formato x@x.x");
            email_textfield.requestFocus();
            return;
        }

        if (!checkPassword(contrasena_textfield.getText())) {
            contrasena_error.setText("Contraseña introducida no es valida, por favor introduce una contraseña que contenga:\n- Entre 8 y 20 caracteres\n- Minimo una letra Mayúscula y Minúscula\n- Minimo un dígito\n- Minimo un caracter de los siguientes: !@#$%&*()-+=\n- No contener ningun espacio en blanco");
            contrasena_textfield.setText("");
            confirmacion_textfield.setText("");
            contrasena_textfield.requestFocus();
            return;
        }

        if (!contrasena_textfield.getText().equals(confirmacion_textfield.getText())) {
            confirmacion_error.setText("No coinciden las contraseñas");
            confirmacion_textfield.setText("");
            confirmacion_textfield.requestFocus();
            return;
        }

        if (Period.between(datePicker.getValue(), LocalDate.now()).getYears() < 16) {
            fecha_error.setText("Debes tener 16 años o mas para poder registrarte");
            return;
        }

        try {
            User nuevoUsuario = modelo.secretario.getNavegacion().registerUser(nickName_textfield.getText(), email_textfield.getText(), contrasena_textfield.getText(), avatar.getImage(), datePicker.getValue());
            modelo.secretario.setUsuario(nuevoUsuario);
            Main.setRoot("PaginaPrincipalUsuario");
        } catch (NavegacionDAOException ex) {
            ex.printStackTrace();
        }
    }

    private void restablecerErrores() {
        nickName_error.setText("");
        email_error.setText("");
        contrasena_error.setText("");
        confirmacion_error.setText("");
        fecha_error.setText("");
    }

    @FXML
    private void cambiarAvatar(ActionEvent event) throws IOException {
        FileChooser selectorArchivo = new FileChooser();
        selectorArchivo.setTitle("Abrir avatar");
        selectorArchivo.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg"));
        File ImagenSeleccionada = selectorArchivo.showOpenDialog(aplicacion.Main.getStage());
        if (ImagenSeleccionada != null) {
            avatar.setImage(new Image(ImagenSeleccionada.toURI().toString()));
        }
    }

}
