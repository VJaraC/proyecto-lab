package proyecto.lab.client.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
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
import proyecto.lab.server.dto.ResumenEquipoDTO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ViewTiempoRealController {

    @FXML
    private Button btnCerrarSesion;

    @FXML
    private TableView<ResumenEquipoDTO> tablaMetricas;

    @FXML
    private TextField txtBuscar;

    @FXML
    private TableColumn<ResumenEquipoDTO, Double> txtCpuMetrica;

    @FXML
    private TableColumn<ResumenEquipoDTO, String> txtEquipoMetrica;

    @FXML
    private Label txtIdEquipo;

    private Timeline autoRefresco;
    private Timeline timelineGraficos;

    @FXML
    private TableColumn<ResumenEquipoDTO, Double> txtRamMetrica;

    @FXML
    private TableColumn<ResumenEquipoDTO, Double> txtDiscoMetrica;

    @FXML
    private Label txtUsuarioSesion;

    @FXML
    private NumberAxis ejeXCPU;

    @FXML
    private NumberAxis ejeXRAM;

    @FXML
    private NumberAxis ejeYCPU;

    @FXML
    private NumberAxis ejeYRAM;

    @FXML
    private LineChart<Number, Number> graficoCPU;

    @FXML
    private LineChart<Number, Number> graficoRam;

    private final XYChart.Series<Number, Number> serieRam = new XYChart.Series<>();
    private final XYChart.Series<Number, Number> serieCpu = new XYChart.Series<>();

    private static final int MAX_X = 60;
    private static final int STEP = 5;
    private static final int PUNTOS_MAX = MAX_X / STEP + 1; // 0..60 cada 5 => 13 puntos

    private final List<Double> histRam = new ArrayList<>();
    private final List<Double> histCpu = new ArrayList<>();

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

    @FXML
    void initialize() {
        txtUsuarioSesion.setText((AppContext.getUsuarioActual().getNombres()));

        txtEquipoMetrica.setCellValueFactory(new PropertyValueFactory<>("hostname"));
        txtDiscoMetrica.setCellValueFactory(new PropertyValueFactory<>("diskActual"));
        txtRamMetrica.setCellValueFactory(new PropertyValueFactory<>("ramActual"));
        txtCpuMetrica.setCellValueFactory(new PropertyValueFactory<>("cpuActual"));

        ActualizarTablaMetricas();
        iniciarRefrescoAutomatico();

        configurarGraficoBase();
        graficoRam.getData().add(serieRam);
        graficoCPU.getData().add(serieCpu);

        iniciarGraficosTiempoReal();
    }

    private void ActualizarTablaMetricas() {
        tablaMetricas.getItems().clear();
        tablaMetricas.getItems().addAll(AppContext.metricas().obtenerResumenEquipo());
        tablaMetricas.refresh();
    }

    public static void animacionTraslacion(Node node) {
        ScaleTransition stIn = new ScaleTransition(Duration.millis(160), node);
        stIn.setToX(1.04);
        stIn.setToY(1.04);

        ScaleTransition stOut = new ScaleTransition(Duration.millis(160), node);
        stOut.setToX(1);
        stOut.setToY(1);

        node.setOnMouseEntered(e -> stIn.playFromStart());
        node.setOnMouseExited(e -> stOut.playFromStart());
    }

    @FXML
    void txtBuscar(ActionEvent event) {
    }

    private void iniciarRefrescoAutomatico() {
        autoRefresco = new Timeline(
                new KeyFrame(
                        Duration.seconds(5),
                        event -> {
                            ResumenEquipoDTO seleccionPrevia = tablaMetricas.getSelectionModel().getSelectedItem();
                            String hostnameSeleccionado = (seleccionPrevia != null) ? seleccionPrevia.getHostname() : null;

                            Task<List<ResumenEquipoDTO>> task = new Task<>() {
                                @Override
                                protected List<ResumenEquipoDTO> call() {
                                    return AppContext.metricas().obtenerResumenEquipo();
                                }
                            };

                            task.setOnSucceeded(ev -> {
                                List<ResumenEquipoDTO> nuevos = task.getValue();
                                tablaMetricas.getItems().setAll(nuevos);

                                if (hostnameSeleccionado != null) {
                                    for (ResumenEquipoDTO item : tablaMetricas.getItems()) {
                                        if (item.getHostname().equals(hostnameSeleccionado)) {
                                            tablaMetricas.getSelectionModel().select(item);
                                            break;
                                        }
                                    }
                                }
                            });

                            task.setOnFailed(ev -> task.getException().printStackTrace());

                            new Thread(task).start();
                        }
                )
        );
        autoRefresco.setCycleCount(Timeline.INDEFINITE);
        autoRefresco.play();
    }

    private void configurarGraficoBase() {
        ejeXRAM.setAutoRanging(false);
        ejeXRAM.setLowerBound(0);
        ejeXRAM.setUpperBound(MAX_X);
        ejeXRAM.setTickUnit(STEP);
        ejeXRAM.setTickLabelsVisible(false);
        ejeXRAM.setTickMarkVisible(false);
        ejeXRAM.setMinorTickVisible(false);

        ejeYRAM.setAutoRanging(false);
        ejeYRAM.setLowerBound(0);
        ejeYRAM.setUpperBound(100);
        ejeYRAM.setTickUnit(20);

        graficoRam.setAnimated(false);
        graficoRam.setLegendVisible(false);

        ejeXCPU.setAutoRanging(false);
        ejeXCPU.setLowerBound(0);
        ejeXCPU.setUpperBound(MAX_X);
        ejeXCPU.setTickUnit(STEP);
        ejeXCPU.setTickLabelsVisible(false);
        ejeXCPU.setTickMarkVisible(false);
        ejeXCPU.setMinorTickVisible(false);

        ejeYCPU.setAutoRanging(false);
        ejeYCPU.setLowerBound(0);
        ejeYCPU.setUpperBound(100);
        ejeYCPU.setTickUnit(20);

        graficoCPU.setAnimated(false);
        graficoCPU.setLegendVisible(false);
    }

    private void iniciarGraficosTiempoReal() {
        timelineGraficos = new Timeline(
                new KeyFrame(Duration.seconds(5), e -> {
                    ResumenEquipoDTO seleccionado = tablaMetricas.getSelectionModel().getSelectedItem();
                    if (seleccionado == null) {
                        return;
                    }

                    double ram = seleccionado.getRamActual();
                    double cpu = seleccionado.getCpuActual();

                    agregarMuestra(ram, cpu);
                })
        );
        timelineGraficos.setCycleCount(Timeline.INDEFINITE);
        timelineGraficos.play();
    }

    private void agregarMuestra(double ram, double cpu) {
        if (histRam.size() == PUNTOS_MAX) {
            histRam.remove(0);
        }
        histRam.add(ram);

        if (histCpu.size() == PUNTOS_MAX) {
            histCpu.remove(0);
        }
        histCpu.add(cpu);

        serieRam.getData().clear();
        for (int i = 0; i < histRam.size(); i++) {
            double x = i * STEP;
            serieRam.getData().add(new XYChart.Data<>(x, histRam.get(i)));
        }

        serieCpu.getData().clear();
        for (int i = 0; i < histCpu.size(); i++) {
            double x = i * STEP;
            serieCpu.getData().add(new XYChart.Data<>(x, histCpu.get(i)));
        }
    }
}
