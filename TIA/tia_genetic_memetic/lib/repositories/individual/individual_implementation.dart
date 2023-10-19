import 'dart:math';

import 'package:tia_genetic_memetic/config/app_config.dart';
import 'package:tia_genetic_memetic/models/graph.dart';
import 'package:tia_genetic_memetic/repositories/individual/individual_interface.dart';

class IndividualImplementation implements IndividualInterface<int> {
  static List<List<double>> distances = List.empty(growable: true);
  static Graph? originalGraph;

  List<int> genotype = List.empty(growable: true);
  double fitness = -1.0;

  IndividualImplementation(int startCity) {
    genotype = List.filled(distances.length, 0, growable: false);
    List<int> cities = getCitiesExcept([startCity]);
    genotype[0] = startCity;
    for (int i = 1; i < genotype.length; i++) {
      genotype[i] = cities.removeAt(0);
    }
    randomise();
  }
  IndividualImplementation._empty() {
    genotype = List.filled(distances.length, 0, growable: false);
  }

  @override
  List<int> getGenotype() => List.from(genotype);

  @override
  List<IndividualInterface<int>> getChildren(IndividualInterface<int> partner) {
    List<IndividualInterface<int>> children = [];
    int blockSize = Random().nextInt(7) + 1;
    List<int> p1 = List.from(genotype);
    List<int> p2 = List.from((partner as IndividualImplementation).genotype);
    IndividualImplementation child = IndividualImplementation._empty();
    bool useParent1 = true;
    for (int i = 0; i < child.genotype.length; i++) {
      if (i % blockSize == 0) useParent1 = !useParent1;
      int gene = useParent1 ? p1.removeAt(0) : p2.removeAt(0);
      p1.remove(gene);
      p2.remove(gene);
      child.genotype[i] = gene;
    }
    children.add(child);
    return children;
  }

  @override
  List<IndividualInterface<int>> getNeighborhood() {
    List<IndividualInterface<int>> neighborhood = [];
    for (int i = 1; i < genotype.length; i++) {
      for (int j = 1; j < genotype.length; j++) {
        if (i != j) {
          IndividualImplementation neighbour =
              copy() as IndividualImplementation;
          int aux = neighbour.genotype[i];
          neighbour.genotype[i] = neighbour.genotype[j];
          neighbour.genotype[j] = aux;
          neighbour.fitness = -1.0;
          neighborhood.add(neighbour);
        }
      }
    }
    return neighborhood;
  }

  @override
  bool isValid() => true;

  @override
  void mutate() {
    Random random = Random();
    int mutations = random
            .nextInt((genotype.length / 10).clamp(1, genotype.length).toInt()) +
        1;
    for (int k = 0; k < mutations; k++) {
      int i, j;
      do {
        i = random.nextInt(genotype.length - 1) + 1;
        j = random.nextInt(genotype.length - 1) + 1;
      } while (i == j);

      int aux = genotype[i];
      genotype[i] = genotype[j];
      genotype[j] = aux;
    }
    fitness = -1.0;
  }

  @override
  double getFitness() {
    if (fitness < 0.0) {
      AppConfig.fitnessCalls++;
      double cost = 0.0;
      for (int i = 0; i < genotype.length - 1; i++) {
        cost += distances[genotype[i]][genotype[i + 1]];
      }
      cost += distances[genotype[genotype.length - 1]][genotype[0]];

      int? visitedEdges = originalGraph?.getVisitedEdges(genotype);
      fitness = cost /
          (visitedEdges == AppConfig.maxNumEdges
              ? (visitedEdges ?? 0) * 1000
              : 1);
    }
    return fitness;
  }

  @override
  IndividualInterface<int> copy() {
    IndividualImplementation ind = IndividualImplementation._empty();
    ind.genotype.setAll(0, genotype);
    ind.fitness = fitness;
    return ind;
  }

  @override
  void randomise() {
    List<int> shuffledGenes = genotype.sublist(1)..shuffle();
    genotype.setAll(1, shuffledGenes);
    fitness = -1.0;
  }

  @override
  int compareTo(IndividualInterface<int> o) =>
      getFitness().compareTo(o.getFitness());

  static IndividualImplementation getGreedySolution(int startCity) {
    IndividualImplementation ind = IndividualImplementation(startCity);
    for (int i = 1; i < ind.genotype.length; i++) {
      ind.genotype[i] =
          getClosestCityExcept(ind.genotype[i - 1], ind.genotype.sublist(0, i));
    }
    return ind;
  }

  static int getClosestCityExcept(int city, List<int> except) {
    double minDistance = double.maxFinite;
    int closest = city;
    for (int i = 0; i < distances.length; i++) {
      if (!except.contains(i) && distances[city][i] < minDistance) {
        closest = i;
        minDistance = distances[city][i];
      }
    }
    return closest;
  }

  static List<int> getCitiesExcept(List<int> except) {
    List<int> res = [];
    for (int i = 0; i < distances.length; i++) {
      if (!except.contains(i)) {
        res.add(i);
      }
    }
    return res;
  }

  @override
  String toString() =>
      'Seed: 1633124807218,  Fitness: ${getFitness()}, Cities:  ${genotype.length},  CppIndividual{genotype=$genotype}';
}
