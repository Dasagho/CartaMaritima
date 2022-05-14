/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;
import java.time.LocalDate;
import java.time.LocalTime;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import model.Session;
/**
 *
 * @author Germán
 */
public class SessionWrapper implements Comparable<SessionWrapper> {
    
    Session session;
    private final ObjectProperty<LocalDate> Fecha = new SimpleObjectProperty<LocalDate>();
    private final ObjectProperty<LocalTime> Hora = new SimpleObjectProperty<LocalTime>();
    private final IntegerProperty Problemas = new SimpleIntegerProperty();
    private final IntegerProperty Aciertos = new SimpleIntegerProperty();
    private final IntegerProperty Fallos = new SimpleIntegerProperty();
    
    public SessionWrapper (Session s) {
        session = s;
        Fecha.setValue(session.getLocalDate());
        Hora.setValue(session.getTimeStamp().toLocalTime());
        Aciertos.setValue(session.getHits());
        Fallos.setValue(session.getFaults());
        Problemas.setValue(Aciertos.getValue() + Fallos.getValue());
    }
    
    public LocalDate getLocalDate() {
        return session.getLocalDate();
    }
    
    public final ObjectProperty FechaProperty() {
		return this.Fecha;
    }
    public final ObjectProperty HoraProperty() {
		return this.Hora;
    }
    public final IntegerProperty ProblemasProperty() {
		return this.Problemas;
    }
    public final IntegerProperty AciertosProperty() {
		return this.Aciertos;
    }
    public final IntegerProperty FallosProperty() {
		return this.Fallos;
    }
    
    public int compareTo(SessionWrapper otherSession) {
        return session.compareTo(otherSession.session);
    }
}
