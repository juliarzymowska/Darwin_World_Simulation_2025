package agh.ics.oop.simulationGUI;

import agh.ics.oop.model.stats.StatsRecord;
import javafx.application.Platform;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.NumberAxis;

public class ChartManager {
    private static final int MAX_DATA_POINTS = 100;
    private static final String X_AXIS_LABEL = "Day";
    private static final String Y_AXIS_LABEL = "Animal Count";
    private static final String SERIES_STYLE = "-fx-stroke: #ff7f0e; -fx-stroke-width: 2px;";

    private final LineChart<Number, Number> chart;
    private XYChart.Series<Number, Number> statsSeries;

    public ChartManager(LineChart<Number, Number> chart) {
        this.chart = chart;
        initializeChart();
    }

    private void initializeChart() {
        Platform.runLater(() -> {
            chart.getData().clear();
            statsSeries = new XYChart.Series<>();
            statsSeries.setName("Population Size");

            chart.setAnimated(false);
            chart.setCreateSymbols(false);

            NumberAxis xAxis = (NumberAxis) chart.getXAxis();
            NumberAxis yAxis = (NumberAxis) chart.getYAxis();

            xAxis.setLabel(X_AXIS_LABEL);
            xAxis.setAutoRanging(false);
            xAxis.setForceZeroInRange(false);

            yAxis.setLabel(Y_AXIS_LABEL);
            yAxis.setAutoRanging(true);

            chart.getData().add(statsSeries);

            if (statsSeries.getNode() != null) {
                statsSeries.getNode().setStyle(SERIES_STYLE);
            }
        });
    }

    public void updateChart(StatsRecord statistics) {
        Platform.runLater(() -> {
            statsSeries.getData().add(new XYChart.Data<>(statistics.day(), statistics.animalCount()));

            if (statsSeries.getData().size() > MAX_DATA_POINTS) {
                statsSeries.getData().remove(0);
            }

            NumberAxis xAxis = (NumberAxis) chart.getXAxis();
            int currentDay = statistics.day();
            int lowerBound = Math.max(1, currentDay - MAX_DATA_POINTS);
            xAxis.setLowerBound(lowerBound);
            xAxis.setUpperBound(Math.max(MAX_DATA_POINTS, currentDay));

            if (statsSeries.getNode() != null) {
                statsSeries.getNode().setStyle(SERIES_STYLE);
            }
        });
    }
}
