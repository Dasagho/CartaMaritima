/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import static aplicacion.Main.setRoot;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import model.Answer;
import model.Problem;

/**
 * FXML Controller class
 *
 * @author david
 */
public class SeleccionTipoPreguntaController implements Initializable {

    @FXML
    private ToggleGroup sel;
    @FXML
    private RadioButton selAleatorio;
    @FXML
    private RadioButton selConcreto;
    
    private ObservableList<Problem> problemas = null;
    @FXML
    private ListView<Problem> vistaDeLista;
    @FXML
    private HBox contenedorEleccion;
    @FXML
    private Button botonAceptar;
    @FXML
    private TextArea enunciado;

    
    private void mostrarEnunciado(int indice) {
        Problem problema = modelo.secretario.getProblemas().get(indice);
        String texto = problema.getText() + "\n";
        List<Answer> respuestas = problema.getAnswers();
        char letra = 'A';
        for (int i = 0; i < respuestas.size(); i++) {
            Answer respuestaX = respuestas.get(i);
            texto = texto + "\n     " + letra + ") " + respuestaX.getText();
            letra++;
        }
        enunciado.setText(texto);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        modelo.secretario.setTitulo("Selección de pregunta");
        
        // Se carga la visualización de la lista
        problemas = FXCollections.observableList(modelo.secretario.getProblemas());
        vistaDeLista.setItems(problemas);
        
        // Se vinculan los radiobotones con el contenedor de problema concreto
        contenedorEleccion.disableProperty().bind(selAleatorio.selectedProperty());
        
        // Deshabilitación del botón Aceptar cuando no hay elección de facto
        selConcreto.selectedProperty().addListener( (observable, preValor, posValor) -> {
            if (posValor && (vistaDeLista.getSelectionModel().getSelectedIndex() ==-1)) {
                botonAceptar.setDisable(true);
            }   if (!posValor) { botonAceptar.setDisable(false);}
        });
        
        // Oyente que controla el campo de texto donde se muestra el enunciado completo
        vistaDeLista.getSelectionModel().selectedIndexProperty().addListener( (obs, preValor, posValor) -> {
           if (posValor.intValue() == -1) {enunciado.setText("");
           } else {
               botonAceptar.setDisable(false);
               mostrarEnunciado(posValor.intValue());}
        });
        
        // Se aplica el formato a la lista
        vistaDeLista.setCellFactory(c -> new ProblemasListCell());
    }

    @FXML
    private void pulsarCancelar(ActionEvent event) throws IOException {
        setRoot("PaginaPrincipalUsuario");
    }

    @FXML
    private void pulsarAceptar(ActionEvent event) throws IOException {
        if (selConcreto.isSelected()){
            modelo.secretario.setIndiceProblemaSel(vistaDeLista.getSelectionModel().getSelectedIndex());
        }   else{
            int indiceAleatorio = (int) Math.floor( Math.random() * problemas.size() );
            modelo.secretario.setIndiceProblemaSel(indiceAleatorio);
        }
        setRoot("ejercicio");
    }

    
    
    class ProblemasListCell extends ListCell<Problem>
    {
        @Override
        protected void updateItem(Problem item, boolean empty)
        {   super.updateItem(item, empty);
            if (item==null) setText(null);
            else setText( (problemas.indexOf(item)+1) + ". " + item.getText().substring(0, 39) + "...");
        }
    }
}
