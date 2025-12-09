package proyecto.lab.client.controllers;

import javafx.animation.KeyFrame;
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
    @FXML private TableColumn<ResumenEquipoDTO, Double> txtCpuMetrica;
    @FXML private TableColumn<ResumenEquipoDTO, String> txtEquipoMetrica;
    @FXML private TableColumn<ResumenEquipoDTO, Double> txtRamMetrica;
    @FXML private TableColumn<ResumenEquipoDTO, Double> txtDiscoMetrica; // Mostrará % Actividad
    @FXML private Label txtIdEquipo;
    @FXML private Label textoDisco;
    @FXML private Label txtUsuarioSesion;

    // Ejes y Gráficos
    @FXML private NumberAxis ejeXCPU, ejeXRAM, ejeYCPU, ejeYRAM;
    @FXML private NumberAxis ejeXTemperatura, ejeYTemperatura;

    @FXML private LineChart<Number, Number> graficoCPU;
    @FXML private LineChart<Number, Number> graficoRam;
    @FXML private LineChart<Number, Number> graficoTemperatura;

    @FXML private PieChart graficoDisco;

    private Timeline autoRefresco;

    // Series
    private final XYChart.Series<Number, Number> serieRam = new XYChart.Series<>();
    private final XYChart.Series<Number, Number> serieCpu = new XYChart.Series<>();
    private final XYChart.Series<Number, Number> serieTemperatura = new XYChart.Series<>();

    // Constantes
    private static final int MAX_X = 15;

    // Clase auxiliar para transportar datos
    private static class DatosGraficos {
        List<PuntoGraficoDTO> historialRam;
        List<PuntoGraficoDTO> historialCpu;
        List<PuntoGraficoDTO> historialDisco;
        List<PuntoGraficoDTO> historialTemperatura;

        public DatosGraficos(List<PuntoGraficoDTO> r, List<PuntoGraficoDTO> c, List<PuntoGraficoDTO> d, List<PuntoGraficoDTO> t) {
            this.historialRam = r;
            this.historialCpu = c;
            this.historialDisco = d;
            this.historialTemperatura = t;
        }
    }

    @FXML
    void initialize() {
        if (AppContext.getUsuarioActual() != null) {
            txtUsuarioSesion.setText(AppContext.getUsuarioActual().getNombres());
        }

        // Configuración de columnas
        txtEquipoMetrica.setCellValueFactory(new PropertyValueFactory<>("hostname"));
        txtRamMetrica.setCellValueFactory(new PropertyValueFactory<>("ramActual"));
        txtCpuMetrica.setCellValueFactory(new PropertyValueFactory<>("cpuActual"));
        // diskActual ahora trae el % de Actividad
        txtDiscoMetrica.setCellValueFactory(new PropertyValueFactory<>("diskActual"));

        ActualizarTablaMetricas();
        configurarGraficoBase();

        // Agregar series
        graficoRam.getData().add(serieRam);
        graficoCPU.getData().add(serieCpu);
        graficoTemperatura.getData().add(serieTemperatura);

        // Listener de selección
        tablaMetricas.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                txtIdEquipo.setText(newVal.getHostname());
                cargarDatosEnSegundoPlano(newVal.getHostname());
            }
        });

        iniciarRefrescoAutomatico();
    }

    private void cargarDatosEnSegundoPlano(String hostname) {
        if (hostname == null || hostname.isEmpty()) return;

        Task<DatosGraficos> tareaCarga = new Task<>() {
            @Override
            protected DatosGraficos call() throws Exception {
                // Consultas usando tus servicios (que ahora llaman a disk_activity)
                List<PuntoGraficoDTO> ram = AppContext.metricas().obtenerRam(hostname);
                List<PuntoGraficoDTO> cpu = AppContext.metricas().obtenerCpu(hostname);
                List<PuntoGraficoDTO> disco = AppContext.metricas().obtenerDisco(hostname);
                List<PuntoGraficoDTO> temp = AppContext.metricas().obtenerCpuTemp(hostname);

                return new DatosGraficos(ram, cpu, disco, temp);
            }
        };

        tareaCarga.setOnSucceeded(event -> {
            DatosGraficos datos = tareaCarga.getValue();
            actualizarSeriesLineales(datos.historialRam, datos.historialCpu, datos.historialTemperatura);
            actualizarGraficoDisco(datos.historialDisco);
        });

        tareaCarga.setOnFailed(event -> {
            Throwable error = tareaCarga.getException();
            error.printStackTrace();
        });

        new Thread(tareaCarga).start();
    }

    private void actualizarSeriesLineales(List<PuntoGraficoDTO> ram, List<PuntoGraficoDTO> cpu, List<PuntoGraficoDTO> temperatura) {
        // RAM
        serieRam.getData().clear();
        if (ram != null) {
            for (int i = 0; i < ram.size(); i++) {
                serieRam.getData().add(new XYChart.Data<>(i, ram.get(i).valor()));
            }
        }

        // CPU
        serieCpu.getData().clear();
        if (cpu != null) {
            for (int i = 0; i < cpu.size(); i++) {
                serieCpu.getData().add(new XYChart.Data<>(i, cpu.get(i).valor()));
            }
        }

        // Temperatura
        serieTemperatura.getData().clear();
        if (temperatura != null) {
            for (int i = 0; i < temperatura.size(); i++) {
                serieTemperatura.getData().add(new XYChart.Data<>(i, temperatura.get(i).valor()));
            }
        }
    }

    // --- AQUÍ ESTÁ EL CAMBIO CLAVE PARA DISK ACTIVITY ---
    private void actualizarGraficoDisco(List<PuntoGraficoDTO> historialDisco) {
        if (historialDisco == null || historialDisco.isEmpty()) {
            graficoDisco.setData(FXCollections.emptyObservableList());
            textoDisco.setText("Sin datos");
            return;
        }

        // Obtenemos el último valor de actividad (0-100%)
        PuntoGraficoDTO ultimoDato = historialDisco.get(historialDisco.size() - 1);
        double actividad = ultimoDato.valor();

        // Calculamos el tiempo inactivo (Idle)
        double inactividad = 100.0 - actividad;
        if (inactividad < 0) inactividad = 0;

        // CAMBIO DE ETIQUETAS: "Activo" vs "Inactivo" (Conceptualmente correcto)
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList(
                new PieChart.Data("Activo (I/O)", actividad),
                new PieChart.Data("Inactivo", inactividad)
        );

        graficoDisco.setData(pieData);

        // Texto descriptivo actualizado
        textoDisco.setText("Actividad de Disco: " + String.format("%.1f", actividad) + "%");
    }

    private void configurarGraficoBase() {
        // Configuración visual de ejes (Ocultar etiquetas X para limpieza)
        configurarEjes(ejeXRAM, ejeYRAM, 100);
        graficoRam.setAnimated(false);
        graficoRam.setLegendVisible(false);

        configurarEjes(ejeXCPU, ejeYCPU, 100);
        graficoCPU.setAnimated(false);
        graficoCPU.setLegendVisible(false);

        configurarEjes(ejeXTemperatura, ejeYTemperatura, 100);
        graficoTemperatura.setAnimated(false);
        graficoTemperatura.setLegendVisible(false);

        // Disco
        graficoDisco.setAnimated(false);
        graficoDisco.setLabelsVisible(false);
        graficoDisco.setLegendVisible(true);
        graficoDisco.setTitle("Carga I/O"); // Título más apropiado
    }

    private void configurarEjes(NumberAxis xAxis, NumberAxis yAxis, int yUpperBound) {
        xAxis.setAutoRanging(false);
        xAxis.setUpperBound(MAX_X);
        xAxis.setTickLabelsVisible(false);
        xAxis.setTickMarkVisible(false);
        xAxis.setMinorTickVisible(false);

        yAxis.setAutoRanging(false);
        yAxis.setUpperBound(yUpperBound);
        yAxis.setLowerBound(0);
        yAxis.setTickUnit(20);
    }

    private void ActualizarTablaMetricas() {
        tablaMetricas.getItems().clear();
        tablaMetricas.getItems().addAll(AppContext.metricas().obtenerResumenEquipo());
        tablaMetricas.refresh();
    }

    private void iniciarRefrescoAutomatico() {
        autoRefresco = new Timeline(
                new KeyFrame(Duration.seconds(5), event -> {
                    ResumenEquipoDTO seleccionPrevia = tablaMetricas.getSelectionModel().getSelectedItem();
                    String hostnameSeleccionado = (seleccionPrevia != null) ? seleccionPrevia.getHostname() : null;

                    Task<List<ResumenEquipoDTO>> task = new Task<>() {
                        @Override protected List<ResumenEquipoDTO> call() {
                            return AppContext.metricas().obtenerResumenEquipo();
                        }
                    };

                    task.setOnSucceeded(ev -> {
                        List<ResumenEquipoDTO> nuevos = task.getValue();
                        if (nuevos.isEmpty() && !tablaMetricas.getItems().isEmpty()) return;

                        tablaMetricas.getItems().setAll(nuevos);

                        if (hostnameSeleccionado != null) {
                            ResumenEquipoDTO nuevoEstado = null;
                            for (ResumenEquipoDTO item : tablaMetricas.getItems()) {
                                if (item.getHostname().equals(hostnameSeleccionado)) {
                                    tablaMetricas.getSelectionModel().select(item);
                                    nuevoEstado = item;
                                    break;
                                }
                            }
                            // Detectar cambios en métricas clave para refrescar gráficos
                            if (nuevoEstado != null && seleccionPrevia != null) {
                                boolean huboCambio = nuevoEstado.getCpuActual() != seleccionPrevia.getCpuActual() ||
                                        nuevoEstado.getRamActual() != seleccionPrevia.getRamActual() ||
                                        nuevoEstado.getDiskActual() != seleccionPrevia.getDiskActual(); // Agregamos disco al trigger

                                if (huboCambio) {
                                    cargarDatosEnSegundoPlano(hostnameSeleccionado);
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
        AppContext.LimpiarSesion(); // Limpiamos datos del usuario

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/IniciarSesion.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setMinWidth(0);
            stage.setMinHeight(0);
            Scene scene = new Scene(root);
            stage.setScene(scene);

            stage.sizeToScene();

            stage.centerOnScreen();
            stage.setTitle("Sistema de Monitoreo - UNAP");
            stage.setResizable(false);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML void txtBuscar(ActionEvent event) {}
}