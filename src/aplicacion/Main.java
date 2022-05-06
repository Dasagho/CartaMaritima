package aplicacion;

import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
        
        Escena = new Scene(loadFXML("inicioSesion"), 900, 600);
        Escena.getStylesheets().add("/resources/estilos.css");
        primaryStage.setScene(Escena);
        primaryStage.show();
        
        // Manejador que captura la peticion de cerrar la ventana y ejecuta un 
        // codigo asociado al acabar
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                System.out.println("Mira mama estoy en " + ((Stage) e.getSource()).getTitle());
                
                Platform.exit();
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        launch();
        
    }
    
}
