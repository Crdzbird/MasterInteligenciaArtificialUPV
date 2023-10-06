/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

/**
 *
 * @author crdzbird
 */
import com.opencsv.CSVWriter;
import java.awt.Color;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * Represents a graph showing the evolution of fitness over iterations.
 */
public class Graph {
    private static final double RANGE_MIN_FACTOR = 0.995;
    private static final double RANGE_MAX_FACTOR = 1.005;

    private static final Logger LOGGER = Logger.getLogger(Graph.class.getName());

    private final XYSeries series;
    private final XYSeriesCollection dataset;
    private final JFreeChart chart;
    private final ChartFrame frame;
    private double min;
    private double max;

    /**
     * Initializes a new Graph.
     */
    public Graph() {
        this.series = new XYSeries("Fitness");
        this.dataset = new XYSeriesCollection(this.series);
        this.chart = ChartFactory.createXYLineChart(
                "Chinese Postman Problem - Fitness Evolution",
                "Iteration",
                "Fitness",
                this.dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                true
        );

        styleChart(this.chart);

        this.frame = new ChartFrame("TIA CLASS", this.chart);
        this.frame.pack();
        this.frame.setVisible(true);
        this.frame.setLocationRelativeTo(null);

        this.min = Double.MAX_VALUE;
        this.max = Double.MIN_VALUE;

        LOGGER.info("Graph initialized and displayed.");
    }

    /**
     * Styles the given chart.
     * @param chart the chart to style.
     */
    private void styleChart(JFreeChart chart) {
        XYPlot plot = chart.getXYPlot();
        plot.getRangeAxis().setAutoRange(true);
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
    }

    /**
     * Adds data to the graph.
     * @param iter the iteration number.
     * @param fitness the fitness value.
     */
    public synchronized void addData(int iter, double fitness) {
        if (min > fitness) {
            min = fitness;
        }
        if (max < fitness) {
            max = fitness;
        }

        (new Thread(() -> {
            series.add(iter, fitness);
            chart.getXYPlot().getRangeAxis().setRange(min * RANGE_MIN_FACTOR, max * RANGE_MAX_FACTOR);
            LOGGER.log(Level.INFO, "Data added to graph: Iteration = {0}, Fitness = {1}", new Object[]{iter, fitness});
        })).start();
    }

    /**
     * Writes the graph data to a file.
     * @param fileName the name of the file.
     * @throws IOException if an I/O error occurs.
     */
    public void writeGraphFile(String fileName) throws IOException {
        try (CSVWriter writer = new CSVWriter(new FileWriter(new File(fileName)))) {
            String[] header = {"Iteration", "Fitness"};
            writer.writeNext(header);

            for (int i = 0; i < series.getItemCount(); i++) {
                double x = series.getX(i).doubleValue();
                double y = series.getY(i).doubleValue();
                String[] data = {String.valueOf(x), String.valueOf(y)};
                writer.writeNext(data);
            }
            LOGGER.log(Level.INFO, "Graph data written to file: {0}", fileName);
        }
    }

    /**
     * Toggles the graph's visibility.
     */
    public void toggleVisibility() {
        this.frame.setVisible(!this.frame.isVisible());
    }

    /**
     * Clears the graph data.
     */
    public void clearData() {
        this.series.clear();
        LOGGER.info("Graph data cleared.");
    }
}
