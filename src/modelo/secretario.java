package modelo;

import DBAccess.NavegacionDAOException;
import java.util.List;
import model.Navegacion;
import static model.Navegacion.getSingletonNavegacion;
import model.Problem;
import model.Session;
import model.User;

public class secretario {

    private static User usuario;
    private static Session sesion;
    private static Navegacion nav;
    private static List<Problem> lista;

    public static void setUsuario(User u) {
        usuario = u;
    }

    public static User getUsuario() {
        return usuario;
    }

    public static void almacenarSesion(Session s) {
        sesion = s;
    }

    public static Session getSesion() {
        return sesion;
    }
    
    public static List<Problem> getProblemas() {
        return lista;
    }

    public static void sumarAcierto() {
    }

    public static void sumarFallo() {
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
