import 'dart:convert';
import 'dart:io';

import 'package:csv/csv.dart';
import 'package:tia_genetic_memetic/algoritms/genetic.dart';
import 'package:tia_genetic_memetic/config/app_config.dart';
import 'package:tia_genetic_memetic/models/city.dart';
import 'package:tia_genetic_memetic/models/graph.dart';
import 'package:tia_genetic_memetic/repositories/elitism/elitism_implementation.dart';
import 'package:tia_genetic_memetic/repositories/individual/individual_implementation.dart';
import 'package:tia_genetic_memetic/repositories/population/population_copy.dart';
import 'package:tia_genetic_memetic/repositories/population/population_interface.dart';
import 'package:tia_genetic_memetic/service/city_dao.dart';

Future<void> main(List<String> arguments) async {
  try {
    await loadCitiesFromFile(AppConfig.heuristicFile);
    await loadCityDistancesFromFile(AppConfig.distanceFile);
    executeAlgorithm();
  } catch (e) {
    print(e);
  }
}

Future<void> loadCitiesFromFile(String path) async {
  final file = File(path); // replace with your file path

  Stream<List<int>> inputStream = file.openRead();

  try {
    int lineCount = 0;
    var lines = inputStream
        .transform(utf8.decoder) // Decode bytes to UTF-8.
        .transform(LineSplitter()); // Convert stream to individual lines.

    await for (var line in lines) {
      lineCount++;

      // Skip the first line if it's a header (or adjust according to how your CSV is structured)
      if (lineCount == 1) continue;

      // Split the line using comma as a delimiter
      var row = line.split(',');

      // Perform your processing with the row here
      City city = City(row[0], double.parse(row[1]), double.parse(row[2]));
      CityDao.create(city);
    }
  } catch (e) {
    print(e.toString());
    // Handle the exception, possibly by rethrowing or logging it
  }
}

Future<void> loadCityDistancesFromFile(String path) async {
  Graph cityGraph = Graph(CityDao.getAll().length);

  final file = File(path); // replace with your file path

  Stream<List<int>> inputStream = file.openRead();

  try {
    int lineCount = 0;
    var lines = inputStream
        .transform(utf8.decoder) // Decode bytes to UTF-8.
        .transform(LineSplitter()); // Convert stream to individual lines.

    await for (var line in lines) {
      lineCount++;

      // Skip the first line if it's a header (or adjust according to how your CSV is structured)
      if (lineCount == 1) continue;

      // Split the line using comma as a delimiter
      var row = line.split(',');

      // Perform your processing with the row here
      int from = CityDao.getIndex(CityDao.get(row[0])!);
      int to = CityDao.getIndex(CityDao.get(row[1])!);
      double distance = double.parse(row[3]);
      cityGraph.setDistance(from, to, distance);
    }

    AppConfig.maxNumEdges =
        lineCount - 2; // Adjust based on how you count the lines
  } catch (e) {
    print(e.toString());
    // Handle the exception, possibly by rethrowing or logging it
  }
  cityGraph.calculateAllPaths();
  IndividualImplementation.distances =
      cityGraph.getFullConnectedGraph(1.0).getDistanceMatrix();
  IndividualImplementation.originalGraph =
      cityGraph; // Assuming IndividualImplementation is a global or static class
}

Future<void> executeAlgorithm() async {
  double computationTime = DateTime.now().millisecondsSinceEpoch.toDouble();

  IndividualImplementation greedy =
      IndividualImplementation.getGreedySolution(AppConfig.mainCity);
  print(greedy.toString());
  print("Best Initial Fitness model: ${greedy.getFitness()}");

  List<IndividualImplementation> initialPopulation =
      buildInitialPopulation(greedy);
  PopulationCopy population =
      PopulationCopy(initialPopulation, ElitismImplementation());
  PopulationCopy populationMP =
      PopulationCopy(initialPopulation, ElitismImplementation());

  PopulationInterface<IndividualImplementation> lastGeneration =
      Genetic(population, memeticPopulation: populationMP)
          .executeWithMemetic(AppConfig.maxIteration);
  displayResults(lastGeneration, computationTime);
}

List<IndividualImplementation> buildInitialPopulation(
    IndividualImplementation greedy) {
  List<IndividualImplementation> initialPopulation = [];
  initialPopulation.add(greedy);
  greedy
      .getNeighborhood()
      .forEach((ind) => initialPopulation.add(ind as IndividualImplementation));
  while (initialPopulation.length < AppConfig.popSize) {
    initialPopulation.add(IndividualImplementation(AppConfig.mainCity));
  }
  return initialPopulation;
}

String generateFileName() {
  return 'MAX_ITER(${AppConfig.maxIteration})-POP_SIZE(${AppConfig.popSize})-MAIN_CITY(${AppConfig.mainCity})-MEME_POP_RATE(${AppConfig.memePopRate})-TEMPERATURE(${AppConfig.temperature.toStringAsFixed(1)})-COOLING_RATE(${AppConfig.coolingRate.toStringAsFixed(1)}).csv';
}

void displayResults(
    PopulationInterface<IndividualImplementation> lastGeneration,
    double startTime) {
  final bestFitness = lastGeneration.retrieveBestIndividual().getFitness();
  print('Fitness calls: ${AppConfig.fitnessCalls}');
  print('Best fitness: $bestFitness');
  print(
      'Computation time (ms): ${DateTime.now().millisecondsSinceEpoch - startTime}');
}
