package aplicacion;

import java.io.IOException;
import java.util.Optional;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author jose
 */
public class Main extends Application {

    private static Scene Escena;
    private static FXMLLoader fxmlLoader; // Pasado a atributo para poder hacer de uso del WindowEvent
    private static Stage stage;

    // Este metodo permite cargar el FXML unicamente pasando como parametro el nombre del
    // archivo sin necesidad de la extension
    private static Parent loadFXML(String fxml) throws IOException {
        fxmlLoader = new FXMLLoader(Main.class.getResource("/vista/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    // Este metodo permite cambiar de escena unicamente declarando el FXML root
    public static void setRoot(String fxml) throws IOException {
        Escena.setRoot(loadFXML(fxml));
    }

    public static Stage getStage() {
        return stage;
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        stage = primaryStage;
        primaryStage.setTitle("Subnautica");

        Escena = new Scene(loadFXML("inicioSesion"), 900, 650);
        primaryStage.setMinHeight(650);
        primaryStage.setMinWidth(900);
        Escena.getStylesheets().add("/resources/estilos.css");
        primaryStage.setScene(Escena);
        primaryStage.show();
        
        // Establecimiento del icono de la aplicación en miniatura
        stage.getIcons().add(new Image("/resources/logotipos/icono-pixelizado.png"));

        // Manejador que captura la peticion de cerrar la ventana y ejecuta un 
        // codigo asociado al acabar
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {

                if (modelo.secretario.usuarioActivo().get()) {
                    Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
                    alerta.setTitle("Cerrar sesión");
                    alerta.setHeaderText("Cerrar sesión");
                    alerta.setContentText("¿Seguro que quieres cerrar la sesión?\n ");
                    Optional<ButtonType> respuesta = alerta.showAndWait();
                    if (respuesta.isPresent() && respuesta.get() == ButtonType.OK) {
                        // Se cierra la sesión
                        modelo.secretario.cerrarSesion();
                        Platform.exit();
                        System.exit(0);
                        
                    } else {
                        e.consume();
                    }
                }

            }
        });
    }

    public static void main(String[] args) {
        launch();

    }

}
