/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/tia_final_project.java to edit this template
 */
package com.crdzbird;

/**
 *
 * @author crdzbird
 */

import com.crdzbird.dao.CityDao;
import com.crdzbird.models.City;
import com.crdzbird.models.Graph;
import com.opencsv.CSVReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import configs.AppConfig;
import ia.Genetic;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import repositories.IndividualImplementation;
import repositories.elitism.ElitismImplementation;
import repositories.population.PopulationCopy;
import repositories.population.PopulationInterface;

public class tia_final_project {

    public static void main(String[] args) {
        try {
            tia_final_project app = new tia_final_project();
            app.loadCitiesFromFile(AppConfig.HEURISTIC_FILE);
            app.loadCityDistancesFromFile(AppConfig.DISTANCE_FILE);
            app.executeAlgorithm();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadCitiesFromFile(String path) throws Exception {
        try (CSVReader reader = new CSVReader(new FileReader(path))) {
            reader.readAll().stream().skip(1).forEach(row -> {
                City city = new City(row[0], Float.parseFloat(row[1]), Float.parseFloat(row[2]));
                CityDao.create(city);
            });
        }
    }

    private void loadCityDistancesFromFile(String path) throws Exception {
        Graph cityGraph = new Graph(CityDao.getAll().size());
        try (CSVReader reader = new CSVReader(new FileReader(path))) {
            List<String[]> rows = reader.readAll();
            AppConfig.maxNumEdges = rows.size() - 2;
            rows.stream().skip(1).forEach(row -> {
                int from = CityDao.getIndex(CityDao.get(row[0]));
                int to = CityDao.getIndex(CityDao.get(row[1]));
                double distance = Double.parseDouble(row[3]);
                cityGraph.setDistance(from, to, distance);
            });
        }
        cityGraph.calculateAllPaths();
        IndividualImplementation.distances = cityGraph.getFullConectedGraph(1.0).getDistanceMatrix();
        IndividualImplementation.originalGraph = cityGraph;
    }

    private void executeAlgorithm() {
        double computationTime = System.currentTimeMillis();

        IndividualImplementation greedy = IndividualImplementation.getGreedySolution(AppConfig.MAIN_CITY);
        System.out.println(greedy.toString());
        System.out.println("Best Initial Fitness model: " + greedy.getFitness());

        List<IndividualImplementation> initialPopulation = buildInitialPopulation(greedy);
        PopulationCopy population = new PopulationCopy(initialPopulation, new ElitismImplementation());
                PopulationCopy populationMP = new PopulationCopy(initialPopulation, new ElitismImplementation());
        ui.FitnessChart graph = new ui.FitnessChart();

                PopulationInterface<IndividualImplementation> lastGeneration = 
                new Genetic<>(population, populationMP, graph).executeWithMemetic(AppConfig.MAX_ITERATION);
//        PopulationInterface<IndividualImplementation> lastGeneration = 
//                (new Memetic<>(population, graph)).execute(
//                        AppConfig.MAX_ITERATION, 
//                        AppConfig.MEME_POP_RATE, 
//                        AppConfig.TEMPERATURE, 
//                        AppConfig.COOLING_RATE);

        try {
            graph.writeGraphFile(generateFileName());
        } catch (IOException ex) {
            Logger.getLogger(tia_final_project.class.getName()).log(Level.SEVERE, null, ex);
        }
        displayResults(lastGeneration, computationTime);
    }

    private List<IndividualImplementation> buildInitialPopulation(IndividualImplementation greedy) {
        List<IndividualImplementation> initialPopulation = new ArrayList<>();
        initialPopulation.add(greedy);
        greedy.getNeighborhood().forEach(ind -> initialPopulation.add((IndividualImplementation) ind));
        while (initialPopulation.size() < AppConfig.POP_SIZE) {
            initialPopulation.add(new IndividualImplementation(AppConfig.MAIN_CITY));
        }
        return initialPopulation;
    }

    private String generateFileName() {
        return String.format("MAX_ITER(%d)-POP_SIZE(%d)-MAIN_CITY(%d)-MEME_POP_RATE(%d)-TEMPERATURE(%.1f)-COOLING_RATE(%.1f).csv", 
                             AppConfig.MAX_ITERATION, AppConfig.POP_SIZE, AppConfig.MAIN_CITY, AppConfig.MEME_POP_RATE, AppConfig.TEMPERATURE, AppConfig.COOLING_RATE);
    }

    private void displayResults(PopulationInterface<IndividualImplementation> lastGeneration, double startTime) {
        System.out.println("Fitness calls: " + AppConfig.fitnessCalls);
        System.out.println("Best fitness: " + lastGeneration.retrieveBestIndividual().getFitness());
        System.out.println("Computation time (ms): " + (System.currentTimeMillis() - startTime));
    }
}
