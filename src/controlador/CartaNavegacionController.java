/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poiupv;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
/**
 *
 * @author Germán
 */
public class CartaNavegacionController implements Initializable{
    
    // hashmap para guardar los Elementos pintados en el mapa
    //private final HashMap<String, Marca> map = new HashMap<>();
    //private ListView<Marca> lista;
    
    // ======================================
    // la variable zoomGroup se utiliza para dar soporte al zoom
    // el escalado se realiza sobre este nodo, al escalar el Group no mueve sus nodos
    private Group zoomGroup;
    private Group zoomGroupMarcas;
    
    @FXML
    private ScrollPane map_scrollpane;
    @FXML
    private Slider zoom_slider;
    @FXML
    private Label posicion;
    @FXML
    private CheckBox mostrarTransportadorID;
    
    //TRANSPORTADOR
    @FXML
    private StackPane transportador;
    @FXML
    private ImageView transportadorImagen;
    double inicioXTrans, inicioYTrans, baseX, baseY;
    
    //MARCAS
    TextField texto;
    Line linea;
    Circle circulo;
    double inicioXArc;
    boolean textoCreado = false;
    
    //MENUS CONTEXTUALES MARCAS
    ContextMenu cM;
    boolean contextMenuCreado = false;
    
    //BOTONES
    Button tipoMarcaActual;
    
    @FXML
    private Button puntoBoton;
    @FXML
    private Button circuloBoton;
    @FXML
    private Button textoBoton;
    @FXML
    private Button lineaBoton;
    @FXML
    private Button panearBoton;
    
    //INSPECTOR
    @FXML
    private Slider sliderInspector;
    @FXML
    private ColorPicker colorInspector;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // enlazamos slider con el zoom
        zoom_slider.valueProperty().addListener((o, oldVal, newVal) -> zoom((Double) newVal));
        
        //=========================================================================
        //Envuelva el contenido de scrollpane en un grupo para que 
        //ScrollPane vuelva a calcular las barras de desplazamiento tras el escalado
        Group contentGroup = new Group();
        zoomGroup = new Group();
        contentGroup.getChildren().add(zoomGroup);
        zoomGroup.getChildren().add(map_scrollpane.getContent());
        map_scrollpane.setContent(contentGroup);
        //Creamos grupo solo para las marcas
        zoomGroupMarcas = new Group();
        zoomGroup.getChildren().add(zoomGroupMarcas);
        //Inicializamos el slider del zoom
        zoom_slider.setMin(0.2);
        zoom_slider.setMax(1.5);
        zoom_slider.setValue(0.2);
        
        //Checkbox de mostrar transportador
        transportadorImagen.visibleProperty().bind(mostrarTransportadorID.selectedProperty());
        transportador.disableProperty().bind(mostrarTransportadorID.selectedProperty().not()); 
    }
    
    @FXML
    private void muestraPosicion(MouseEvent event) {
        posicion.setText("Longitud:" + (int) event.getX() + ",          Latitud:" + (int) event.getY());
    }
    
    //ZOOM -----------------------------------
    @FXML
    void zoomIn(ActionEvent event) {
        //================================================
        // el incremento del zoom dependerá de los parametros del 
        // slider y del resultado esperado
        double sliderVal = zoom_slider.getValue();
        zoom_slider.setValue(sliderVal += 0.1);
    }

    @FXML
    void zoomOut(ActionEvent event) {
        double sliderVal = zoom_slider.getValue();
        zoom_slider.setValue(sliderVal + -0.1);
    }
    
    // esta funcion es invocada al cambiar el value del slider zoom_slider
    private void zoom(double scaleValue) {
        //===================================================
        //guardamos los valores del scroll antes del escalado
        double scrollH = map_scrollpane.getHvalue();
        double scrollV = map_scrollpane.getVvalue();
        //===================================================
        // escalamos el zoomGroup en X e Y con el valor de entrada
        zoomGroup.setScaleX(scaleValue);
        zoomGroup.setScaleY(scaleValue);
        //===================================================
        // recuperamos el valor del scroll antes del escalado
        map_scrollpane.setHvalue(scrollH);
        map_scrollpane.setVvalue(scrollV);
    }

    //BOTONES MENU ARRIBA -----------------------------------
    @FXML
    private void cerrarAplicacion(ActionEvent event) {
        ((Stage)zoom_slider.getScene().getWindow()).close();
    }
    
    @FXML
    private void acercaDe(ActionEvent event) {
        Alert mensaje= new Alert(Alert.AlertType.INFORMATION);
        mensaje.setTitle("Acerca de");
        mensaje.setHeaderText("NOSOTROS - 2022");
        mensaje.showAndWait();
    }

    //TRANSPORTADOR -----------------------------------
    @FXML
    private void transportadorDragged(MouseEvent event) {
        double despX = event.getSceneX() - inicioXTrans;
        double despY = event.getSceneY() - inicioYTrans;
        
        double X = despX + baseX;
        double Y = despY + baseY;
        
        double transLayoutX = transportador.getLayoutX();
        double transWidth = transportador.getWidth();
        double transLayoutY = transportador.getLayoutY();
        double transHeight = transportador.getHeight();
        
        Bounds parentBounds = transportador.getParent().getLayoutBounds();

        if ((transLayoutX + X > -1) && (transLayoutX + X < parentBounds.getWidth() - transWidth)) {
                transportador.setTranslateX(X);
            } else if (transLayoutX + (X) < 0) {
                transportador.setTranslateX(-transLayoutX);
            } else {
                transportador.setTranslateX(parentBounds.getWidth() - transLayoutX - transWidth);
            }
        
        if ((transLayoutY + Y < parentBounds.getHeight() - transHeight) && (transLayoutY + Y > -1)) {
                transportador.setTranslateY(Y);
            } else if (transLayoutY + Y < 0) {
                transportador.setTranslateY(-transLayoutY);
            } else {
                transportador.setTranslateY(parentBounds.getHeight() - transLayoutY - transHeight);
            }
        event.consume();
    }
    @FXML
    private void transportadorPressed(MouseEvent event) {
        inicioXTrans = event.getSceneX();
        inicioYTrans = event.getSceneY();
        baseX = transportador.getTranslateX();
        baseY = transportador.getTranslateY();
        event.consume();
    }

    //BOTONES HERRAMIENTAS MARCAS -----------------------------------
    @FXML
    private void seleccionarTipoMarca(ActionEvent event) {
        map_scrollpane.setPannable(false);
        tipoMarcaActual = (Button)event.getSource();
        if (tipoMarcaActual == panearBoton)  map_scrollpane.setPannable(true);
    }
    //PULSAR EN EL MAPA Y CREAR MARCAS -----------------------------------
    @FXML
    private void ratonPulsadoMapa(MouseEvent event) {
        if (textoCreado) {
                crearTexto(new ActionEvent());
                return;
        }
        if (event.getButton() == MouseButton.PRIMARY){
            if (tipoMarcaActual == textoBoton) {
                    //Si está seleccionada texto, creo un TextField y lo pinta con los datos leídos del inspector
                    texto = new TextField();
                    zoomGroupMarcas.getChildren().add(texto);
                    texto.setLayoutX(event.getX());
                    texto.setLayoutY(event.getY());
                    Font nuevaFuente = Font.font(3*sliderInspector.getValue());
                    texto.setFont(nuevaFuente);
                    texto.requestFocus();
                    texto.setOnAction(this::crearTexto);
                    textoCreado = true;
                    event.consume();
            } else if (tipoMarcaActual == puntoBoton) {
                //Si está seleccionado el punto, creo un punto y lo pinta con los datos leídos del inspector
                Circle punto = new Circle(1);
                aplicarValoresPunto(punto);
                zoomGroupMarcas.getChildren().add(punto);
                punto.setOnContextMenuRequested(this::crearMenuContextual);
                punto.setOnMousePressed(this::seleccionarElemento);
                punto.setCenterX(event.getX());
                punto.setCenterY(event.getY());
                event.consume();
            } else if (tipoMarcaActual == lineaBoton) {
                //Si está seleccionada la línea, crea el inicio de una línea y la pinta con los datos leídos del inspector
                linea = new Line(event.getX(),event.getY(),event.getX(),event.getY());
                aplicarValoresLinea(linea);
                zoomGroupMarcas.getChildren().add(linea);
                linea.setOnContextMenuRequested(this::crearMenuContextual);
                linea.setOnMousePressed(this::seleccionarElemento);
                event.consume();
            } else if (tipoMarcaActual == circuloBoton){
                //Si está seleccionado el círculo, crea un circulo y lo pinta con los datos leídos del inspector
                circulo = new Circle(1);
                circulo.setFill(Color.TRANSPARENT);
                aplicarValoresCirculo(circulo);
                zoomGroupMarcas.getChildren().add(circulo);
                circulo.setOnContextMenuRequested(this::crearMenuContextual);
                circulo.setOnMousePressed(this::seleccionarElemento);
                circulo.setCenterX(event.getX());
                circulo.setCenterY(event.getY());
                inicioXArc = event.getX();
                event.consume();
            }
        }
    }
    @FXML
    private void ratonMantenidoMapa(MouseEvent event) {
        if (tipoMarcaActual == lineaBoton) {
            if (linea != null) {
                linea.setEndX(event.getX());
                linea.setEndY(event.getY());
                event.consume();
            }
        } else if (tipoMarcaActual == circuloBoton){
            if (circulo != null) {
                double radio = Math.abs(event.getX()-inicioXArc);
                circulo.setRadius(radio);
                event.consume();
            }
        }
    }
    @FXML
    private void ratonSoltadoMapa(MouseEvent event) {
        linea = null;
        circulo = null;
    }
    
    //EVENTOS MARCAS -----------------------------------
    @FXML
    private void borrarTodo(ActionEvent event) {
        if (zoomGroupMarcas.getChildren().size() > 0) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Borrar todo");
            alert.setHeaderText("Borrar todas las marcas");
            alert.setContentText("¿Seguro que quiere borrar todas las marcas?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK){
                zoomGroupMarcas.getChildren().clear();
            }
        } else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Borrar todo");
            alert.setHeaderText("Borrar todas las marcas");
            alert.setContentText("Ahora mismo no existen marcas para borrar.");
            alert.showAndWait();
        }
    }
    private void seleccionarElemento(MouseEvent e) {
        if (e.getButton() == MouseButton.PRIMARY){
            Node n = (Node)e.getSource();
            editarNodo(n);
        }
    }
    private void crearTexto(ActionEvent e){
        if (!texto.getText().isBlank()){
            Text textoT = new Text(texto.getText());
            textoT.setX(texto.getLayoutX());
            textoT.setY(texto.getLayoutY());
            aplicarValoresTexto(textoT);
            zoomGroupMarcas.getChildren().add(textoT);
            textoT.setOnContextMenuRequested(this::crearMenuContextual);
            textoT.setOnMousePressed(this::seleccionarElemento);
        }
        zoomGroupMarcas.getChildren().remove(texto);
        textoCreado = false;
        e.consume();
    }
    private void crearMenuContextual (ContextMenuEvent e){
        if (!contextMenuCreado){
            contextMenuCreado = true;
            cM = new ContextMenu();
            MenuItem editarItem = new MenuItem("Editar con valores seleccionados");
            MenuItem borrarItem = new MenuItem("Eliminar");
            cM.getItems().add(editarItem);
            cM.getItems().add(borrarItem);
            
            editarItem.setOnAction(ev -> {
                Node n = (Node)e.getSource();
                editarNodo(n);
                e.consume();
            });
            
            borrarItem.setOnAction(ev -> {
                zoomGroupMarcas.getChildren().remove((Node)e.getSource());
                ev.consume();
            });
            
            cM.setOnHidden(ev -> {
                contextMenuCreado = false;
            });
        } 
        if (cM!=null)
        cM.show((Node)e.getSource(),e.getScreenX(), e.getScreenY());
        e.consume();
    }
    //METODOS AYUDA CREAR Y EDITAR MARCAS ------------------------------
    private void editarNodo(Node n) {
        if (n instanceof Text){
                Text texto = (Text) n;
                aplicarValoresTexto(texto);
        } else if (n instanceof Circle){
            Circle circle = (Circle) n;
            if (circle.getFill() == Color.TRANSPARENT) {
                //Si es un circulo
                aplicarValoresCirculo(circle);
            } else {
                //Si es un punto
                aplicarValoresPunto(circle);
            }
        } else if (n instanceof Line) {
            Line line = (Line) n;
            aplicarValoresLinea(line);
        }
    }
    
    private void aplicarValoresTexto(Text t) {
        t.setFill(colorInspector.getValue());
        Font nuevaFuente = Font.font(3*sliderInspector.getValue());
        t.setFont(nuevaFuente);
    }
    
    private void aplicarValoresLinea(Line l) {
        l.setFill(colorInspector.getValue());
        l.setStroke(colorInspector.getValue());
        l.setStrokeWidth(sliderInspector.getValue());
    }
    
    private void aplicarValoresCirculo(Circle c) {
        c.setStroke(colorInspector.getValue());
        c.setStrokeWidth(sliderInspector.getValue());
    }
    
    private void aplicarValoresPunto(Circle c) {
        c.setFill(colorInspector.getValue());
        c.setStroke(colorInspector.getValue());
        c.setRadius(sliderInspector.getValue());
    }
    
}
