/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import static aplicacion.Main.setRoot;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.Session;
import modelo.SessionWrapper;
import modelo.secretario;

/**
 * FXML Controller class
 *
 * @author david
 */
public class ResultadosUsuarioController implements Initializable {

    @FXML
    private Button pulsarVolver;
    @FXML
    private DatePicker fechaDesde;
    @FXML
    private DatePicker fechaHasta;
    
    //TABLA PRINCIPAL Y COLUMNAS
    @FXML
    private TableView<SessionWrapper> tabla;
    @FXML
    private TableColumn<SessionWrapper, LocalDate> columnaDia;
    @FXML
    private TableColumn<SessionWrapper, LocalTime> columnaHora;
    @FXML
    private TableColumn<SessionWrapper, Integer> columnaProblemas;
    @FXML
    private TableColumn<SessionWrapper, Integer> columnaAciertos;
    @FXML
    private TableColumn<SessionWrapper, Integer> columnaFallos;
    
    //Lista sesiones
    List<SessionWrapper> listaSesionesWrapper;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //PREPARAR LISTA Y LISTA DE SESSIONWRAPPER -------------------------------------
        List<Session> listaSesiones = secretario.getSesiones();
        
        listaSesionesWrapper = new ArrayList<SessionWrapper>();
        for (int i = 0; i < listaSesiones.size(); i++) {
            SessionWrapper sw = new SessionWrapper(listaSesiones.get(i));
            listaSesionesWrapper.add(sw);
        }
        
        //PREPARAR COLUMNAS TABLE VIEW -------------------------------------
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        columnaDia.setCellValueFactory(cellData -> cellData.getValue().FechaProperty()); 
        columnaDia.setCellFactory(columna -> {
            return new TableCell<SessionWrapper,LocalDate> () {
                @Override
                protected void updateItem(LocalDate d, boolean empty) {
                    super.updateItem(d, empty);
                    if (empty) setText("");
                    else {
                        setText(formatoFecha.format(d));
                    }
                }
            };
        });
        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm:ss");
        columnaHora.setCellValueFactory(cellData -> cellData.getValue().HoraProperty());
        columnaHora.setCellFactory(columna -> {
            return new TableCell<SessionWrapper,LocalTime> () {
                @Override
                protected void updateItem(LocalTime t, boolean empty) {
                    super.updateItem(t, empty);
                    if (empty) setText("");
                    else {
                        setText(formatoHora.format(t));
                    }
                }
            };
        });
        columnaProblemas.setCellValueFactory(cellData -> cellData.getValue().ProblemasProperty().asObject());
        columnaAciertos.setCellValueFactory(cellData -> cellData.getValue().AciertosProperty().asObject());
        columnaFallos.setCellValueFactory(cellData -> cellData.getValue().FallosProperty().asObject());
        
        
        //INICIALIZAR DATA PICKERS --------------------------------------------------------------------------------------
        //Inicializar data picker "Desde" con el día que se hizo la primera sesión
        LocalDate diaPrimeraSesion = listaSesiones.get(0).getLocalDate();
        fechaDesde.setValue(diaPrimeraSesion);
        
        //Inicializar data picker "Hasta" con el día actual, y bloquear los días seleccionables a partir del día de "Desde" hasta el día de hoy
        fechaHasta.setValue(LocalDate.now());
        
        //Bloquear los días anteriores del primer día para que no sean seleccionables y los posteriores al día de hoy
        fechaDesde.setDayCellFactory((DatePicker picker) -> {
            return new DateCell() {
                @Override
                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    setDisable(empty || date.compareTo(fechaHasta.getValue()) > 0 || date.compareTo(diaPrimeraSesion) < 0);
                    actualizarLista();
                }
            };
        });
        
        //Bloquear los días anteiores al fechaDesde, y posteriores al día de hoy
        fechaHasta.setDayCellFactory((DatePicker picker) -> {
            return new DateCell() {
                @Override
                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    setDisable(empty || date.compareTo(LocalDate.now()) > 0 || date.compareTo(fechaDesde.getValue()) < 0);
                    actualizarLista();
                }
            };
        });
        //--------------------------------------------------------------------------------------
        actualizarLista();
        
    }
    
    //GESTIONAR LISTA ----------------------------
    private void actualizarLista() {
        LocalDate fechaDesdeDia = fechaDesde.getValue();
        LocalDate fechaHastaDia = fechaHasta.getValue();
        tabla.getItems().clear();
        for (int i = 0; i < listaSesionesWrapper.size(); i++) {
            SessionWrapper s = listaSesionesWrapper.get(i);
            LocalDate l = s.getLocalDate();
            if (l.compareTo(fechaDesdeDia) >= 0 && l.compareTo(fechaHastaDia) <= 0) {
                tabla.getItems().add(s);
            }
        }
    }
    
    //BOTON VOLVER ---------------------------
    @FXML
    private void pulsarVolver(ActionEvent event) throws IOException {
        setRoot("PaginaPrincipalUsuario");
    }
}
