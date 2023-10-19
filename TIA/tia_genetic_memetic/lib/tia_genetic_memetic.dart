import 'dart:convert';
import 'dart:developer';
import 'dart:io';

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
    log(e.toString());
  }
}

Future<void> loadCitiesFromFile(String path) async {
  final file = File(path);

  Stream<List<int>> inputStream = file.openRead();

  try {
    int lineCount = 0;
    var lines = inputStream.transform(utf8.decoder).transform(LineSplitter());

    await for (var line in lines) {
      lineCount++;

      if (lineCount == 1) continue;

      var row = line.split(',');

      City city = City(row[0], double.parse(row[1]), double.parse(row[2]));
      CityDao.create(city);
    }
  } catch (e) {
    log(e.toString());
  }
}

Future<void> loadCityDistancesFromFile(String path) async {
  Graph cityGraph = Graph(CityDao.getAll().length);

  final file = File(path);

  Stream<List<int>> inputStream = file.openRead();

  try {
    int lineCount = 0;
    var lines = inputStream.transform(utf8.decoder).transform(LineSplitter());

    await for (var line in lines) {
      lineCount++;
      if (lineCount == 1) continue;
      var row = line.split(',');
      int from = CityDao.getIndex(CityDao.get(row[0])!);
      int to = CityDao.getIndex(CityDao.get(row[1])!);
      double distance = double.parse(row[3]);
      cityGraph.setDistance(from, to, distance);
    }
    AppConfig.maxNumEdges = lineCount - 2;
  } catch (e) {
    log(e.toString());
  }
  cityGraph.calculateAllPaths();
  IndividualImplementation.distances =
      cityGraph.getFullConnectedGraph(1.0).getDistanceMatrix();
  IndividualImplementation.originalGraph = cityGraph;
}

Future<void> executeAlgorithm() async {
  double computationTime = DateTime.now().millisecondsSinceEpoch.toDouble();

  IndividualImplementation greedy =
      IndividualImplementation.getGreedySolution(AppConfig.mainCity);
  log(greedy.toString());
  log("Best Initial Fitness model: ${greedy.getFitness()}");

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

void displayResults(
    PopulationInterface<IndividualImplementation> lastGeneration,
    double startTime) {
  final bestFitness = lastGeneration.retrieveBestIndividual().getFitness();
  log('Fitness calls: ${AppConfig.fitnessCalls}');
  log('Best fitness: $bestFitness');
  log('Computation time (ms): ${DateTime.now().millisecondsSinceEpoch - startTime}');
}
