package proyecto.lab.client.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Duration;
import proyecto.lab.client.application.AppContext;
import proyecto.lab.server.dto.PuntoGraficoDTO;
import proyecto.lab.server.dto.ResumenEquipoDTO;

import java.io.IOException;
import java.util.List;

public class ViewTiempoRealController {

    @FXML private Button btnCerrarSesion;
    @FXML private TableView<ResumenEquipoDTO> tablaMetricas;
    @FXML private TextField txtBuscar;
    @FXML private TableColumn<ResumenEquipoDTO, Double> txtCpuMetrica;
    @FXML private TableColumn<ResumenEquipoDTO, String> txtEquipoMetrica;
    @FXML private TableColumn<ResumenEquipoDTO, Double> txtRamMetrica;
    @FXML private TableColumn<ResumenEquipoDTO, Double> txtDiscoMetrica;
    @FXML private Label txtIdEquipo;
    @FXML private Label textoDisco;
    @FXML private Label txtUsuarioSesion;

    // Ejes y Gráficos
    @FXML private NumberAxis ejeXCPU, ejeXRAM, ejeYCPU, ejeYRAM;
    @FXML private LineChart<Number, Number> graficoCPU;
    @FXML private LineChart<Number, Number> graficoRam;
    @FXML private PieChart graficoDisco;

    private Timeline autoRefresco;
    private Timeline timelineGraficos;

    // Series
    private final XYChart.Series<Number, Number> serieRam = new XYChart.Series<>();
    private final XYChart.Series<Number, Number> serieCpu = new XYChart.Series<>();

    // Constantes
    private static final int MAX_X = 15;
    private static final int STEP = 1;

    // Clase auxiliar para transportar datos desde el hilo de fondo a la UI
    private static class DatosGraficos {
        List<PuntoGraficoDTO> historialRam;
        List<PuntoGraficoDTO> historialCpu;
        List<PuntoGraficoDTO> historialDisco; // Usaremos el último valor de esta lista

        public DatosGraficos(List<PuntoGraficoDTO> r, List<PuntoGraficoDTO> c, List<PuntoGraficoDTO> d) {
            this.historialRam = r;
            this.historialCpu = c;
            this.historialDisco = d;
        }
    }

    @FXML
    void initialize() {
        if (AppContext.getUsuarioActual() != null) {
            txtUsuarioSesion.setText(AppContext.getUsuarioActual().getNombres());
        }

        txtEquipoMetrica.setCellValueFactory(new PropertyValueFactory<>("hostname"));
        txtDiscoMetrica.setCellValueFactory(new PropertyValueFactory<>("diskActual"));
        txtRamMetrica.setCellValueFactory(new PropertyValueFactory<>("ramActual"));
        txtCpuMetrica.setCellValueFactory(new PropertyValueFactory<>("cpuActual"));

        ActualizarTablaMetricas();
        configurarGraficoBase();

        graficoRam.getData().add(serieRam);
        graficoCPU.getData().add(serieCpu);

        // LISTENER: Al hacer clic, cargamos datos en segundo plano
        tablaMetricas.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                txtIdEquipo.setText(newVal.getHostname());
                cargarDatosEnSegundoPlano(newVal.getHostname());
            }
        });

        iniciarRefrescoAutomatico(); // Tabla
        iniciarGraficosTiempoReal(); // Gráficos
    }

    // --- OPTIMIZACIÓN CON TASK (Adiós al LAG) ---

    private void iniciarGraficosTiempoReal() {
        // Refresco cada 30 segundos
        timelineGraficos = new Timeline(
                new KeyFrame(Duration.seconds(30), e -> {
                    ResumenEquipoDTO seleccionado = tablaMetricas.getSelectionModel().getSelectedItem();
                    if (seleccionado != null) {
                        cargarDatosEnSegundoPlano(seleccionado.getHostname());
                    }
                })
        );
        timelineGraficos.setCycleCount(Timeline.INDEFINITE);
        timelineGraficos.play();
    }

    private void cargarDatosEnSegundoPlano(String hostname) {
        if (hostname == null || hostname.isEmpty()) return;

        // 1. Creamos la Tarea (Se ejecuta en un hilo separado)
        Task<DatosGraficos> tareaCarga = new Task<>() {
            @Override
            protected DatosGraficos call() throws Exception {
                // AQUÍ van las consultas lentas a la BD. No bloquean la UI.
                List<PuntoGraficoDTO> ram = AppContext.metricas().obtenerRam(hostname);
                List<PuntoGraficoDTO> cpu = AppContext.metricas().obtenerCpu(hostname);
                List<PuntoGraficoDTO> disco = AppContext.metricas().obtenerDisco(hostname);
                return new DatosGraficos(ram, cpu, disco);
            }
        };

        // 2. Cuando la tarea termina con éxito (Volvemos al hilo de la UI)
        tareaCarga.setOnSucceeded(event -> {
            DatosGraficos datos = tareaCarga.getValue();

            // Actualizar Gráficos Lineales
            actualizarSeriesLineales(datos.historialRam, datos.historialCpu);

            // Actualizar Gráfico de Pastel (Ocupado vs Libre)
            actualizarGraficoDisco(datos.historialDisco);
        });

        tareaCarga.setOnFailed(event -> {
            Throwable error = tareaCarga.getException();
            error.printStackTrace(); // O mostrar un mensaje de error en log
        });

        // 3. Iniciamos el hilo
        new Thread(tareaCarga).start();
    }

    // --- LÓGICA VISUAL ---

    private void actualizarSeriesLineales(List<PuntoGraficoDTO> ram, List<PuntoGraficoDTO> cpu) {
        serieRam.getData().clear();
        for (int i = 0; i < ram.size(); i++) {
            serieRam.getData().add(new XYChart.Data<>(i, ram.get(i).valor()));
        }

        serieCpu.getData().clear();
        for (int i = 0; i < cpu.size(); i++) {
            serieCpu.getData().add(new XYChart.Data<>(i, cpu.get(i).valor()));
        }
    }

    private void actualizarGraficoDisco(List<PuntoGraficoDTO> historialDisco) {
        // Obtenemos el dato más reciente
        if (historialDisco == null || historialDisco.isEmpty()) {
            graficoDisco.setData(FXCollections.emptyObservableList());
            textoDisco.setText("Sin datos");
            return;
        }

        // Suponemos que la lista viene ordenada por fecha desc (el DAO lo hacía así antes de invertir)
        // o tomamos el último de la lista.
        // Si usas mi código anterior que hacía Collections.reverse, el ÚLTIMO elemento es el más reciente.
        PuntoGraficoDTO ultimoDato = historialDisco.get(historialDisco.size() - 1);

        double porcentajeOcupado = ultimoDato.valor(); // ej: 75.0
        double porcentajeLibre = 100.0 - porcentajeOcupado; // ej: 25.0

        if (porcentajeLibre < 0) porcentajeLibre = 0; // Protección por si viene > 100%

        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList(
                new PieChart.Data("Ocupado", porcentajeOcupado),
                new PieChart.Data("Libre", porcentajeLibre)
        );

        graficoDisco.setData(pieData);

        // Colorear las porciones (Opcional, para asegurar consistencia visual)
        // Nota: Esto debe hacerse después de añadir los datos al gráfico
        /*
        pieData.get(0).getNode().setStyle("-fx-pie-color: #ff6b6b;"); // Ocupado (Rojo)
        pieData.get(1).getNode().setStyle("-fx-pie-color: #51cf66;"); // Libre (Verde)
        */

        textoDisco.setText("Uso: " + String.format("%.1f", porcentajeOcupado) + "%");
    }

    private void configurarGraficoBase() {
        // RAM
        ejeXRAM.setAutoRanging(false);
        ejeXRAM.setUpperBound(MAX_X);
        ejeXRAM.setTickLabelsVisible(false);
        ejeXRAM.setTickMarkVisible(false);
        ejeXRAM.setMinorTickVisible(false);
        ejeYRAM.setAutoRanging(false);
        ejeYRAM.setUpperBound(100);
        ejeYRAM.setTickUnit(20);
        graficoRam.setAnimated(false);
        graficoRam.setLegendVisible(false);

        // CPU
        ejeXCPU.setAutoRanging(false);
        ejeXCPU.setUpperBound(MAX_X);
        ejeXCPU.setTickLabelsVisible(false);
        ejeXCPU.setTickMarkVisible(false);
        ejeXCPU.setMinorTickVisible(false);
        ejeYCPU.setAutoRanging(false);
        ejeYCPU.setUpperBound(100);
        ejeYCPU.setTickUnit(20);
        graficoCPU.setAnimated(false);
        graficoCPU.setLegendVisible(false);

        // Disco
        graficoDisco.setAnimated(false);
        graficoDisco.setLabelsVisible(false); // Ocultar etiquetas flotantes si se ven feas
        graficoDisco.setLegendVisible(true);
    }

    // --- TABLA Y UTILIDADES ---

    private void ActualizarTablaMetricas() {
        // También podríamos usar Task aquí si la carga inicial es lenta
        tablaMetricas.getItems().clear();
        tablaMetricas.getItems().addAll(AppContext.metricas().obtenerResumenEquipo());
        tablaMetricas.refresh();
    }

    private void iniciarRefrescoAutomatico() {
        autoRefresco = new Timeline(
                new KeyFrame(Duration.seconds(5), event -> {
                    // Refresh de la tabla también en background para evitar micro-cortes
                    Task<List<ResumenEquipoDTO>> task = new Task<>() {
                        @Override protected List<ResumenEquipoDTO> call() {
                            return AppContext.metricas().obtenerResumenEquipo();
                        }
                    };

                    task.setOnSucceeded(ev -> {
                        ResumenEquipoDTO seleccion = tablaMetricas.getSelectionModel().getSelectedItem();
                        String host = (seleccion != null) ? seleccion.getHostname() : null;

                        tablaMetricas.getItems().setAll(task.getValue());

                        if (host != null) {
                            for (ResumenEquipoDTO r : tablaMetricas.getItems()) {
                                if (r.getHostname().equals(host)) {
                                    tablaMetricas.getSelectionModel().select(r);
                                    break;
                                }
                            }
                        }
                    });
                    new Thread(task).start();
                })
        );
        autoRefresco.setCycleCount(Timeline.INDEFINITE);
        autoRefresco.play();
    }

    @FXML
    void btnCerrarSesion(ActionEvent event) {
        AppContext.LimpiarSesion();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/IniciarSesion.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Sistema de Monitoreo - UNAP");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML void txtBuscar(ActionEvent event) {}

    public static void animacionTraslacion(Node node) {
        ScaleTransition stIn = new ScaleTransition(Duration.millis(160), node);
        stIn.setToX(1.04); stIn.setToY(1.04);
        ScaleTransition stOut = new ScaleTransition(Duration.millis(160), node);
        stOut.setToX(1); stOut.setToY(1);
        node.setOnMouseEntered(e -> stIn.playFromStart());
        node.setOnMouseExited(e -> stOut.playFromStart());
    }
}