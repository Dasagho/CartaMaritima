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
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
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
    @FXML
    private Label nombre_label;
    @FXML
    private Label correo_label;
    @FXML
    private Label contrasena_label;
    @FXML
    private Label confirmar_label;
    @FXML
    private Label fecha_label;
    @FXML
    private GridPane grid;

    // private boolean enEdicion = false;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        // Nombramiento de la ventana
        if (!modelo.secretario.usuarioActivo().getValue())  modelo.secretario.setTitulo("Registrarse");

        // Asignacion de la fecha actual al datePicker para evitar errores de NullPointer
        datePicker.setValue(LocalDate.now());

        // Diferenciamos pagina de registro de Modificar perfil
        nickName_textfield.disableProperty().bind(secretario.usuarioActivo());
        titulo.setText(secretario.usuarioActivo().getValue() ? "MODIFICAR PERFIL" : "REGISTRARSE");

        // Notificamos de los errores al usuario al perder el foco
        nickName_textfield.focusedProperty().addListener((obs, oldVal, newVal) -> {
            modelo.secretario.animacion(newVal, nombre_label);
            comprobarErrores();
        });
        email_textfield.focusedProperty().addListener((obs, oldVal, newVal) -> {
            modelo.secretario.animacion(newVal, correo_label);
            comprobarErrores();
        });
        contrasena_textfield.focusedProperty().addListener((obs, oldVal, newVal) -> {
            modelo.secretario.animacion(newVal, contrasena_label);
            comprobarErrores();
        });
        confirmacion_textfield.focusedProperty().addListener((obs, oldVal, newVal) -> {
            modelo.secretario.animacion(newVal, confirmar_label);
            comprobarErrores();
        });
        datePicker.focusedProperty().addListener((obs, oldVal, newVal) -> {
            modelo.secretario.animacion(newVal, fecha_label);
            comprobarErrores();
        });

        // Bloqueamos los dias posteriores a hoy
        datePicker.setDayCellFactory((DatePicker picker) -> {
            return new DateCell() {
                @Override
                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    setDisable(empty || date.compareTo(datePicker.getValue()) > 0);
                }
            };
        });
    }

    public void initEdicion() { // Invocado desde la pantalla principal de usuario
        // enEdicion = true;
        // modelo.secretario.setTitulo("Editar Perfil");
        titulo.setText("EDITAR PERFIL");
        // System.out.println("Correcto");
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
        //restablecerErrores();

        if (!checkNickName(nickName_textfield.getText())) {
            nickName_error.setText("Nombre de usuario no válido, un nombre válido debe tener entre 6 y 15 carácteres y solo puede contener mayúsculas, minúsculas, guiones o guiones bajos");
            nickName_textfield.requestFocus();
            return;
        }

        if (!User.checkEmail(email_textfield.getText())) {
            email_error.setText("Formato del correo electrónico introducido no es válido por favor introduce un correo válido con formato x@x.x");
            email_textfield.requestFocus();
            return;
        }

        if (!checkPassword(contrasena_textfield.getText())) {
            contrasena_error.setText("Introduce una contraseña que contenga: Entre 8 y 20 carácteres, mínimo una letra mayúscula y minúscula, un dígito, un carácter de los siguientes: !@#$%&*()-+= y no contener ningún espacio en blanco");
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
            fecha_error.setText("Debes tener 16 años o más para poder registrarte");
            return;
        }

        if (modelo.secretario.usuarioActivo().getValue()) {                             // <- Comprueba si está registrando un usuario o modificando uno existente

            // Se solicita confirmación para guardar cambios
            Alert alerta1 = new Alert(AlertType.CONFIRMATION);
            alerta1.setTitle("Guardar cambios");
            alerta1.setHeaderText("Guardar cambios");
            alerta1.setContentText("¿Deseas guardar los cambios?\n ");
            Optional<ButtonType> respuesta = alerta1.showAndWait();

            if (respuesta.isPresent() && respuesta.get() == ButtonType.OK) {
                try {
                    modelo.secretario.getUsuario().setEmail(email_textfield.getText());
                    modelo.secretario.getUsuario().setPassword(contrasena_textfield.getText());
                    modelo.secretario.getUsuario().setBirthdate(datePicker.getValue());
                    modelo.secretario.getUsuario().setAvatar(avatar.getImage());
                    modelo.secretario.iniciarSesion();
                    ((Button) event.getSource()).getScene().getWindow().hide();

                } catch (NavegacionDAOException ex) {
                    Logger.getLogger(registrarseController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        } else {

            // Se solicita confirmación para registrarse
            Alert alerta2 = new Alert(AlertType.CONFIRMATION);
            alerta2.setTitle("Nuevo usuario");
            alerta2.setHeaderText("Nuevo usuario");
            alerta2.setContentText("Se creará un nuevo usuario\n ");
            Optional<ButtonType> respuesta = alerta2.showAndWait();

            if (respuesta.isPresent() && respuesta.get() == ButtonType.OK) {
                try {
                    User nuevoUsuario = modelo.secretario.getNavegacion().registerUser(nickName_textfield.getText(), email_textfield.getText(), contrasena_textfield.getText(), avatar.getImage(), datePicker.getValue());

                    // Se informa de que la cuenta ha sido creada
                    System.out.println("Correctísimo");
                    Alert alerta3 = new Alert(AlertType.INFORMATION);
                    alerta3.setTitle(null);
                    alerta3.setHeaderText(null);
                    alerta3.setContentText("Cuenta creada correctamente");
                    alerta3.show();

                    modelo.secretario.setUsuario(nuevoUsuario);
                    modelo.secretario.iniciarSesion();
                    Main.setRoot("PaginaPrincipalUsuario");
                } catch (NavegacionDAOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private void comprobarErrores() {
        if (!checkNickName(nickName_textfield.getText()) && !nickName_textfield.getText().isEmpty()) {
            nickName_error.setText("Nombre de usuario no válido, un nombre válido debe tener entre 6 y 15 carácteres y solo puede contener mayúsculas, minúsculas, guiones o guiones bajos");
            return;
        } else {
            nickName_error.setText("");
        }

        if (!User.checkEmail(email_textfield.getText()) && !email_textfield.getText().isEmpty()) {
            email_error.setText("Formato del correo electrónico introducido no es válido por favor introduce un correo válido con formato x@x.x");
            return;
        } else {
            email_error.setText("");
        }

        if (!checkPassword(contrasena_textfield.getText()) && !contrasena_textfield.getText().isBlank()) {
            contrasena_error.setText("Introduce una contraseña que contenga: Entre 8 y 20 carácteres, mínimo una letra mayúscula y minúscula, un dígito, un carácter de los siguientes: !@#$%&*()-+= y no contener ningún espacio en blanco");
            return;
        } else {
            contrasena_error.setText("");
        }

        if (!contrasena_textfield.getText().equals(confirmacion_textfield.getText()) && !confirmacion_textfield.getText().isEmpty()) {
            confirmacion_error.setText("No coinciden las contraseñas");
            return;
        } else {
            confirmacion_error.setText("");
        }
    }

    private void restablecerErrores() {
        nickName_error.setText("");
        email_error.setText("");
        contrasena_error.setText("");
        confirmacion_error.setText("");
        fecha_error.setText("");
    }

    /**
     * *
     * Falta por modificar el ultimo avatar que debe ser un icono de añadir tu
     * avatar personalizado
     *
     * @param event
     * @throws IOException
     * @throws URISyntaxException
     */
    @FXML
    private void cambiarAvatar(ActionEvent event) throws IOException, URISyntaxException {
        // Recuperamos la lista de los avatares por defecto de /resources
        List<File> imagenesDefecto = recuperarAvataresPorDefecto();

        // Creamos el container que almacenará los botones de seleccion de avatar
        GridPane grid = new GridPane();

        // Creamos la lista de ImageView en funcion de las imagenes por defecto que tengamos
        ImageView[] images = new ImageView[imagenesDefecto.size()];

        // Inicializamos los ImageView y les asignamos los avatares por defecto
        for (int i = 0; i < imagenesDefecto.size(); i++) {
            images[i] = new ImageView(new Image("file:" + imagenesDefecto.get(i).toString()));
            images[i].setFitHeight(70);
            images[i].setFitWidth(70);
            images[i].setPreserveRatio(true);
        }

        int col = 0;
        int fil = 0;

        // Recorremos el grid creando los botones con los ImageView incorporados, además de asignarles su comportamiento
        for (int i = 0; i < imagenesDefecto.size(); i++) {

            if (col % 3 == 0) {
                col = 0;
                fil++;
            }

            Button boton = new Button();
            boton.setGraphic(images[i]);
            boton.setOnAction(col == 2 && fil == 2 ? this::anadirAvatarPersonalizado : this::seleccionarAvatar);
            grid.add(boton, col, fil);
            grid.setHalignment(boton, HPos.CENTER);

            col++;
        }

        grid.setHgap(5);
        grid.setVgap(5);
        grid.setAlignment(Pos.CENTER);
        // for (ColumnConstraints cc : grid.getColumnConstraints()) cc.setHalignment(HPos.CENTER);
        grid.styleProperty().setValue("-fx-background-color: #007e97");

        // for (RowConstraints rc : grid.getRowConstraints()) rc.setValignment(VPos.CENTER);
        // Finalmente creamos la escena y le agregamos dimensiones y el grid
        Scene nuevaEscena = new Scene(grid, 300, 200);
        Stage nuevaVentana = new Stage();
        nuevaVentana.setScene(nuevaEscena);
        nuevaVentana.initModality(Modality.APPLICATION_MODAL);
        nuevaVentana.showAndWait();

    }

    public void seleccionarAvatar(ActionEvent event) {
        Button botonSel = (Button) event.getSource();
        ImageView avatarSel = (ImageView) botonSel.getGraphic();
        Image archivo = avatarSel.getImage();
        // System.out.println(archivo.getUrl());
        avatar.setImage(archivo);
        ((Button) event.getSource()).getScene().getWindow().hide();
    }

    public void anadirAvatarPersonalizado(ActionEvent event) {
        FileChooser selectorArchivo = new FileChooser();
        selectorArchivo.setTitle("Abrir avatar");
        selectorArchivo.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg"));
        File ImagenSeleccionada
                = selectorArchivo.showOpenDialog(aplicacion.Main.getStage());
        if (ImagenSeleccionada != null) {
            avatar.setImage(new Image(ImagenSeleccionada.toURI().toString()));
            ((Button) event.getSource()).getScene().getWindow().hide();
        }
    }

    private List<File> recuperarAvataresPorDefecto() throws URISyntaxException {
        Path pathPackage = new File(Main.class.getResource("/resources/avatars/avatar1.png").toURI()).getParentFile().toPath();
        File file = new File(pathPackage.toString());
        String[] s = file.list();
        Arrays.sort(s);
        File[] imagenes = new File[s.length];
        for (int i = 0; i < s.length; i++) {
            imagenes[i] = new File(Main.class.getResource("/resources/avatars/" + s[i]).toURI());
        }

        List<File> imagenesDefecto = Arrays.asList(imagenes);
        return imagenesDefecto;
    }

}
