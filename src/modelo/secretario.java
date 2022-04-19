package modelo;

import model.Session;
import model.User;

public class secretario {

    private static User usuario;
    private static Session sesion;

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

    public static void sumarAcierto() {
    }

    public static void sumarFallo() {
    }

}
