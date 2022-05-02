package modelo;

import DBAccess.NavegacionDAOException;
import java.time.LocalDateTime;
import static java.time.LocalDateTime.now;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import model.Navegacion;
import static model.Navegacion.getSingletonNavegacion;
import model.Problem;
import model.Session;
import model.User;

public class secretario {

    private static User usuario;
    private static Navegacion nav;
    private static List<Problem> lista;
    
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
    public static void iniciarSesion(){
        fotoTemporal = now();
    }
    
    public static void sumarAcierto() { aciertos++; }
    public static void sumarFallo() { fallos++; }
    public static int getAciertos(){return aciertos; }
    public static int getFallos(){return fallos; }
    
    public static void cerrarSesion(){
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

    /**
     * Objeto de Navegacion intermediaria entre la DB y nuestros controladores
     *
     * @return nav
     */
    public static Navegacion getNavegacion() {
        return nav;
    }
    
    public static List<Problem> getProblemas() {
        return lista;
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
