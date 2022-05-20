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
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.Session;
import modelo.SessionWrapper;
import modelo.secretario;

/**
 * FXML Controller class
 *
 * @author Germán
 */
public class ResultadosUsuarioController implements Initializable {

    @FXML
    private Button pulsarVolver;
    
    //DATA PICKERS
    @FXML
    private DatePicker fechaDesde;
    @FXML
    private DatePicker fechaHasta;
    @FXML
    private Label desdeLabel;
    @FXML
    private Label hastaLabel;
    
    //FORMATOS FECHA Y HORA
    DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm:ss");
    
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
    
    //LISTA SESIONES (CON CLASE ENVOLTORIO)
    List<SessionWrapper> listaSesionesWrapper;
    
    //LABELS MOSTRAR DATOS
    int actualProblemas, actualAciertos, actualFallos;
    @FXML
    private Label totalSesionesID;
    @FXML
    private Label totalProblemasID;
    @FXML
    private Label totalAciertosID;
    @FXML
    private Label totalFallosID;
    @FXML
    private Label seleccionadoSesionesID;
    @FXML
    private Label seleccionadoProblemasID;
    @FXML
    private Label seleccionadoAciertosID;
    @FXML
    private Label seleccionadoFallosID;

    //GRAFICO 
    @FXML
    private BarChart<SessionWrapper, Integer> grafico;
    private XYChart.Series serieAciertos;
    private XYChart.Series serieFallos;

    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        serieAciertos = new XYChart.Series();
        serieFallos = new XYChart.Series();
        serieAciertos.setName("Aciertos");
        serieFallos.setName("Fallos");
        
        //PREPARAR LISTA Y LISTA DE SESSIONWRAPPER -------------------------------------
        List<Session> listaSesiones = secretario.getSesiones();
        listaSesionesWrapper = new ArrayList<SessionWrapper>();
        for (int i = 0; i < listaSesiones.size(); i++) {
            SessionWrapper sw = new SessionWrapper(listaSesiones.get(i));
            if (sw.ProblemasProperty().getValue() > 0)
                listaSesionesWrapper.add(sw);
        }
        
        //PREPARAR COLUMNAS TABLE VIEW -------------------------------------
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
                }
            };
        });
        //--------------------------------------------------------------------------------------
        actualizarListaYDatos();
        rellenarDatosGrafica();
        actualizarDatosTotales();
        actualizarDatosSeleccionados();
        
        //Animaciones labels ------------------------------------------------------------------
        fechaDesde.focusedProperty().addListener((obs, oldVal, newVal) -> {modelo.secretario.animacion(newVal, desdeLabel);});
        fechaHasta.focusedProperty().addListener((obs, oldVal, newVal) -> {modelo.secretario.animacion(newVal, hastaLabel);});
        
        secretario.setTitulo("Ver Resultados");
    }
    
    @FXML
    private void dataPickerCambiado(ActionEvent event) {
        actualizarListaYDatos();
        rellenarDatosGrafica();
        actualizarDatosSeleccionados();
    }
    
    //GESTIONAR LISTA ----------------------------
    private void actualizarListaYDatos() {
        int aciertos;
        int fallos;
        int problemas;
        //Primero se reinician valores
        actualProblemas = 0;
        actualAciertos = 0;
        actualFallos = 0;
        
        LocalDate fechaDesdeDia = fechaDesde.getValue();
        LocalDate fechaHastaDia = fechaHasta.getValue();

        tabla.getItems().clear();
        //For para recorrer todas las sesiones del usuario actual y ver cuales son las que están el rango de fechas seleccionadas
        for (int i = 0; i < listaSesionesWrapper.size() ; i++) {
            SessionWrapper s = listaSesionesWrapper.get(i);
            LocalDate l = s.getLocalDate();
            //Si la sesión entra en las fechas indicadas
            if (l.compareTo(fechaDesdeDia) >= 0 && l.compareTo(fechaHastaDia) <= 0) {
                //Añadimos a la tabla
                tabla.getItems().add(s);
                //Cambiamos datos a mostrar
                aciertos = s.AciertosProperty().getValue(); 
                fallos = s.FallosProperty().getValue();
                problemas = s.ProblemasProperty().getValue();
                actualAciertos += aciertos;
                actualFallos += fallos;
                actualProblemas += problemas;
            }
        }
    }
    
    private void rellenarDatosGrafica() {
        //Primero se reinician valores
        int maximoY = 0;
        int aciertos = 0;
        int fallos = 0;
        grafico.getData().clear();
        serieAciertos.getData().clear();
        serieFallos.getData().clear();
        //Se recorren los elementos actuales en la tabla, se y se generan los datos necesarios para el gráfico (dos series, una para aciertos y otra para fallos)
        for (int i = 0; i < tabla.getItems().size() ; i++) {
            SessionWrapper s = tabla.getItems().get(i);
            aciertos = s.AciertosProperty().getValue();
            fallos = s.FallosProperty().getValue();
            serieAciertos.getData().add(new XYChart.Data("Sesión del día\n" + formatoFecha.format(s.getLocalDate()) + "\n"+ formatoHora.format((LocalTime)s.HoraProperty().getValue()), aciertos));
            serieFallos.getData().add(new XYChart.Data("Sesión del día\n" + formatoFecha.format(s.getLocalDate()) + "\n"+ formatoHora.format((LocalTime)s.HoraProperty().getValue()), fallos));
            if (aciertos > maximoY){
                maximoY = aciertos;
            }
            if (fallos > maximoY) {
                maximoY = fallos;
            }
        }
        //Se añaden los datos en el gráfico
        grafico.getData().add(serieAciertos);
        grafico.getData().add(serieFallos);
    }
    
    //ACTUALIZAR VISUALIZACION DE DATOS TOTALES Y DE FECHAS SELECCIONADAS -------------------------
    private void actualizarDatosTotales() {
        String strSesiones = String.valueOf(listaSesionesWrapper.size());
        String strProblemas = String.valueOf(secretario.getTotalProblemasRealizados());
        String strAciertos = String.valueOf(secretario.getTotalAciertos());
        String strFallos = String.valueOf(secretario.getTotalFallos());
        totalSesionesID.setText(strSesiones);
        totalProblemasID.setText(strProblemas);
        totalAciertosID.setText(strAciertos);
        totalFallosID.setText(strFallos);
    }
    
    private void actualizarDatosSeleccionados() {
        String strSesiones = String.valueOf(tabla.getItems().size());
        String strProblemas = String.valueOf(actualProblemas);
        String strAciertos = String.valueOf(actualAciertos);
        String strFallos = String.valueOf(actualFallos);
        seleccionadoSesionesID.setText(strSesiones);
        seleccionadoProblemasID.setText(strProblemas);
        seleccionadoAciertosID.setText(strAciertos);
        seleccionadoFallosID.setText(strFallos);
    }
    
    //BOTON VOLVER ---------------------------
    @FXML
    private void pulsarVolver(ActionEvent event) throws IOException {
        setRoot("PaginaPrincipalUsuario");
    }
}
