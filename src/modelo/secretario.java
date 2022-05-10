package modelo;

import DBAccess.NavegacionDAOException;
import java.time.LocalDateTime;
import static java.time.LocalDateTime.now;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import javafx.util.Duration;
import model.Navegacion;
import static model.Navegacion.getSingletonNavegacion;
import model.Problem;
import model.Session;
import model.User;

public class secretario {

    private static User usuario;
    private static Navegacion nav;
    private static List<Problem> lista;
    private static int indiceProblemaSel;

// - - - - - Atributos de sesión - - - - - - - - -
    private static LocalDateTime fotoTemporal;
    private static int aciertos = 0;
    private static int fallos = 0;
// - - - - - - - - - - - - - - - - - - - - - - - - 
    private static BooleanProperty usuarioActivado = new SimpleBooleanProperty(false);   // <- Inicializamos la propiedad booleana

    public static void setUsuario(User u) {
        usuario = u;
        usuarioActivado.setValue(Boolean.TRUE);     // <- Aviso de que hay un usuario activo
    }

    public static User getUsuario() {
        return usuario;
    }

    public static BooleanProperty usuarioActivo() {
        return usuarioActivado;
    }

// - - - - - Métodos de sesión - - - - - - - - -
    public static void iniciarSesion() {
        fotoTemporal = now();
    }

    public static void sumarAcierto() {
        aciertos++;
    }

    public static void sumarFallo() {
        fallos++;
    }

    public static int getAciertos() {
        return aciertos;
    }

    public static int getFallos() {
        return fallos;
    }

    public static void cerrarSesion() {
        // Almacenar objeto sesion en la base de datos
        Session s = new Session(fotoTemporal, aciertos, fallos);
        try {
            usuario.addSession(s);
            usuarioActivado.setValue(Boolean.FALSE);    // <- Aviso de que ya no hay un usuario activo

        } catch (NavegacionDAOException ex) {
            System.out.println("No ha sido posible conectarse a la base de datos");
            ex.printStackTrace();
        }
    }
// - - - - - - - - - - - - - - - - - - - - - - - - 

    public static List<Problem> getProblemas() {
        return lista;
    }

    public static int getIndiceProblemaSel() {
        return indiceProblemaSel;
    }

    public static void setIndiceProblemaSel(int i) {
        indiceProblemaSel = i;
    }

// - - - - - - - Animaciones - - - - - - - - - - - - - - - 
    public static void animacion(Boolean newVal, Node nodo) {
        if (newVal) {
            
            // Creacion de Animacion de traslacion que sube un poco al nodo referenciado y lo desplaza a la izquierda
            TranslateTransition translateTransition = new TranslateTransition(Duration.millis(200), nodo);
            translateTransition.setFromY(0);
            translateTransition.setToY(-5.0);
            translateTransition.setFromX(1.0);
            translateTransition.setToX(-65);

            // Creacion de Animacion de scalado para agrandar la letra
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), nodo);
            scaleTransition.setFromX(1);
            scaleTransition.setFromY(1);
            scaleTransition.setToX(1.3);
            scaleTransition.setToY(1.3);

            // Añadimos ambas animaciones a un ParallelTransition para que ejecuten al mismo tiempo
            ParallelTransition parallelTransition = new ParallelTransition();
            parallelTransition.getChildren().addAll(translateTransition, scaleTransition);

            parallelTransition.play();

        } else { // La inversa de cada animacion para que se recoloquen cuando pierdan el foco

            TranslateTransition translateTransition = new TranslateTransition(Duration.millis(200), nodo);
            translateTransition.setFromY(-5.0);
            translateTransition.setToY(0.0);
            
            translateTransition.setFromX(-65);
            translateTransition.setToX(1.0);

            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), nodo);
            scaleTransition.setFromX(1.3);
            scaleTransition.setFromY(1.3);
            scaleTransition.setToX(1);
            scaleTransition.setToY(1);

            ParallelTransition parallelTransition = new ParallelTransition();
            parallelTransition.getChildren().addAll(translateTransition, scaleTransition);

            parallelTransition.play();

        }
    }
// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

    /**
     * Objeto de Navegacion intermediaria entre la DB y nuestros controladores
     *
     * @return nav
     */
    public static Navegacion getNavegacion() {
        return nav;
    }

    /**
     * Metodo inicializador del Singleton capaz de comunicarse con la base de
     * datos y proveer sus metodos
     */
    public static void initialize() {
        try {
            nav = getSingletonNavegacion();

        } catch (NavegacionDAOException ex) {
            System.out.println("No ha sido posible conectarse a la base de datos");
            ex.printStackTrace();
        }
        lista = nav.getProblems();
    }

    // Modificar para que el titulo quede algo como: Subnautica - Iniciar sesion
    // modelo.secretario.setTitulo("");
    /**
     * format(param) -> aplicacion.Main.getStage().setTitle(param)
     */
    public static void setTitulo(String titulo) {
        aplicacion.Main.getStage().setTitle("Subnautica" + " - " + titulo);
    }
}
