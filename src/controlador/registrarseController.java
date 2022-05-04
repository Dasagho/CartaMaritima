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
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.User;
import static model.User.checkNickName;
import static model.User.checkPassword;
import modelo.secretario;

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
    @FXML
    private Label titulo;

    // private boolean enEdicion = false;
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

        // Diferenciamos pagina de registro de Modificar perfil
        nickName_textfield.disableProperty().bind(secretario.usuarioActivo());
        titulo.setText(secretario.usuarioActivo().getValue() ? "Modificar Perfil" : "Registrarse");

    }

    public void initEdicion() { // Invocado desde la pantalla principal de usuario
        // enEdicion = true;
        modelo.secretario.setTitulo("Editar Perfil");
        System.out.println("Correcto");
        nickName_textfield.setText(modelo.secretario.getUsuario().getNickName());
        // nickName_textfield.setEditable(false);
        email_textfield.setText(modelo.secretario.getUsuario().getEmail());
        contrasena_textfield.setText(modelo.secretario.getUsuario().getPassword());
        confirmacion_textfield.setText(modelo.secretario.getUsuario().getPassword());
        datePicker.setValue(modelo.secretario.getUsuario().getBirthdate());
        avatar.setImage(modelo.secretario.getUsuario().getAvatar());

    }

    @FXML
    private void cancelar(ActionEvent event) throws IOException {
        if (secretario.usuarioActivo().getValue()) {                // <- Dividimos el comportamiento segun el contexto
            ((Node) (event.getSource())).getScene().getWindow().hide();

        } else {
            restablecerErrores();
            Main.setRoot("inicioSesion");
        }
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

        if (modelo.secretario.usuarioActivo().getValue()) {                             // <- Comprueba si está registrando un usuario o modificando uno existente
            try {
                modelo.secretario.getUsuario().setEmail(email_textfield.getText());
                modelo.secretario.getUsuario().setPassword(contrasena_textfield.getText());
                modelo.secretario.getUsuario().setBirthdate(datePicker.getValue());
                modelo.secretario.getUsuario().setAvatar(avatar.getImage());
                modelo.secretario.iniciarSesion();

            } catch (NavegacionDAOException ex) {
                Logger.getLogger(registrarseController.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            try {
                User nuevoUsuario = modelo.secretario.getNavegacion().registerUser(nickName_textfield.getText(), email_textfield.getText(), contrasena_textfield.getText(), avatar.getImage(), datePicker.getValue());
                modelo.secretario.setUsuario(nuevoUsuario);
                modelo.secretario.iniciarSesion();
                Main.setRoot("PaginaPrincipalUsuario");

            } catch (NavegacionDAOException ex) {
                ex.printStackTrace();
            }
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
    private void cambiarAvatar(ActionEvent event) throws IOException, URISyntaxException {
        Path pathPackage = new File(Main.class.getResource("/resources/avatars/avatar1.png").toURI()).getParentFile().toPath();

        File file = new File(pathPackage.toString());
        String[] s = file.list();
        File[] imagenes = new File[s.length];
        for (int i = 0; i < s.length; i++) {
            imagenes[i] = new File(Main.class.getResource("/resources/avatars/" + s[i]).toURI());
        }

        List<File> imagenesDefecto = Arrays.asList(imagenes);
        for (File f : imagenesDefecto) {
            System.out.println(f.toString());
        }
        System.out.println(imagenesDefecto.size());

        GridPane grid = new GridPane();
        grid.setHgap(5);
        grid.setVgap(5);

        ImageView[] images = new ImageView[imagenesDefecto.size()];

        for (int i = 0; i < imagenesDefecto.size(); i++) {
            images[i] = new ImageView(new Image("file:" + imagenesDefecto.get(i).toString()));
            images[i].setFitHeight(70);
            images[i].setFitWidth(70);
            images[i].setPreserveRatio(true);
        }

        int col = 0;
        int fil = 0;
        
        for (int i = 0; i < imagenesDefecto.size(); i++) {
           
            if (col > 2) { col = 0; fil++; }
            grid.add((images[i]), col, fil);
            // System.out.println("file:" + imagenesDefecto.get(i).getAbsolutePath());
            col++;
        }

        Scene nuevaEscena = new Scene(grid, 220, 145);
        Stage nuevaVentana = new Stage();
        nuevaVentana.setScene(nuevaEscena);

        nuevaVentana.showAndWait();
        // avatar.setImage(new Image("file:" + imagenesDefecto.get(0).toString()));
        System.out.println(grid.getColumnCount() + ", " + grid.getRowCount());

//        FileChooser selectorArchivo = new FileChooser();
//        selectorArchivo.setTitle("Abrir avatar");
//        selectorArchivo.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg"));
//        File ImagenSeleccionada
//                = selectorArchivo.showOpenDialog(aplicacion.Main.getStage());
//        if (ImagenSeleccionada != null) {
//            avatar.setImage(new Image(ImagenSeleccionada.toURI().toString()));
//        }
//        System.out.println(ImagenSeleccionada.toURI());
    }

}
