import 'dart:math';

import 'package:tia_genetic_memetic/repositories/elitism/elitism_abstraction.dart';
import 'package:tia_genetic_memetic/repositories/individual/individual_implementation.dart';

class ElitismImplementation
    implements ElitismInterface<IndividualImplementation> {
  @override
  List<IndividualImplementation> elitism(
      List<IndividualImplementation> population, bool compact) {
    List<IndividualImplementation> nextGeneration = List.from(population)
      ..sort();
    int eliteCount = (population.length * 0.2).round();
    nextGeneration = nextGeneration.sublist(0, eliteCount);
    int startingCity = population[0].getGenotype()[0];
    addNewCppIndividuals(nextGeneration, startingCity, eliteCount);
    while (nextGeneration.length < population.length) {
      IndividualImplementation parent1 = selectRandomIndividual(population);
      IndividualImplementation parent2 = selectRandomIndividual(population);
      nextGeneration
          .add(parent1.getChildren(parent2)[0] as IndividualImplementation);
    }
    if (compact) {
      Set<IndividualImplementation> compactedPop = Set.from(nextGeneration);
      nextGeneration.clear();
      nextGeneration.addAll(compactedPop);
      addNewCppIndividuals(nextGeneration, startingCity,
          population.length - nextGeneration.length);
    }
    nextGeneration.sort();
    return nextGeneration;
  }

  void addNewCppIndividuals(
      List<IndividualImplementation> list, int startingCity, int count) {
    for (int i = 0; i < count; i++) {
      list.add(IndividualImplementation(startingCity));
    }
  }

  IndividualImplementation selectRandomIndividual(
      List<IndividualImplementation> list) {
    Random random = Random();
    return list[random.nextInt(list.length)];
  }
}
