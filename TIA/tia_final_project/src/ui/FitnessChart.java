package ui;

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

public class FitnessChart {

    private static final double RANGE_MIN_FACTOR = 0.995;
    private static final double RANGE_MAX_FACTOR = 1.005;
    private static final Logger LOGGER = Logger.getLogger(FitnessChart.class.getName());

    private final XYSeries seriesGenetic;
    private final XYSeries seriesMemetic;

    private final XYSeriesCollection dataset;
    private final JFreeChart chart;
    private final ChartFrame frame;
    private double min;
    private double max;

    public FitnessChart() {
        this.seriesGenetic = new XYSeries("Genetic Fitness");
        this.seriesMemetic = new XYSeries("Memetic Fitness");
        
        this.dataset = new XYSeriesCollection();
        dataset.addSeries(this.seriesGenetic);
        dataset.addSeries(this.seriesMemetic);
        
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

    private void styleChart(JFreeChart chart) {
        XYPlot plot = chart.getXYPlot();
        plot.getRangeAxis().setAutoRange(true);
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
    }

    public synchronized void addData(int iter, double fitnessGenetic, double fitnessMemetic) {
        updateMinMax(fitnessGenetic);
        updateMinMax(fitnessMemetic);

        (new Thread(() -> {
            seriesGenetic.add(iter, fitnessGenetic);
            seriesMemetic.add(iter, fitnessMemetic);
            chart.getXYPlot().getRangeAxis().setRange(min * RANGE_MIN_FACTOR, max * RANGE_MAX_FACTOR);
            LOGGER.log(Level.INFO, "Data added to graph: Iteration = {0}, Fitness Genetic = {1}, Fitness Memetic = {2}", 
                                    new Object[]{iter, fitnessGenetic, fitnessMemetic});
        })).start();
    }

    private void updateMinMax(double fitness) {
        if (min > fitness) {
            min = fitness;
        }
        if (max < fitness) {
            max = fitness;
        }
    }

    public void writeGraphFile(String fileName) throws IOException {
        try (CSVWriter writer = new CSVWriter(new FileWriter(new File(fileName)))) {
            String[] header = {"Iteration", "Fitness Genetic", "Fitness Memetic"};
            writer.writeNext(header);

            int itemCount = Math.max(seriesGenetic.getItemCount(), seriesMemetic.getItemCount());
            for (int i = 0; i < itemCount; i++) {
                double xGenetic = i < seriesGenetic.getItemCount() ? seriesGenetic.getX(i).doubleValue() : Double.NaN;
                double yGenetic = i < seriesGenetic.getItemCount() ? seriesGenetic.getY(i).doubleValue() : Double.NaN;

                double xMemetic = i < seriesMemetic.getItemCount() ? seriesMemetic.getX(i).doubleValue() : Double.NaN;
                double yMemetic = i < seriesMemetic.getItemCount() ? seriesMemetic.getY(i).doubleValue() : Double.NaN;
                
                String[] data = {String.valueOf(i), String.valueOf(yGenetic), String.valueOf(yMemetic)};
                writer.writeNext(data);
            }
            LOGGER.log(Level.INFO, "Graph data written to file: {0}", fileName);
        }
    }

    public void toggleVisibility() {
        this.frame.setVisible(!this.frame.isVisible());
    }

    public void clearData() {
        this.seriesGenetic.clear();
        this.seriesMemetic.clear();
        LOGGER.info("Graph data cleared.");
    }
}
