import 'dart:developer';

import 'package:tia_genetic_memetic/config/app_config.dart';
import 'package:tia_genetic_memetic/repositories/individual/individual_abstraction.dart';
import 'package:tia_genetic_memetic/repositories/population/population_abstraction.dart';

class Genetic<I extends IndividualInterface, P extends PopulationInterface<I>> {
  final P population;
  P? memeticPopulation;

  Genetic(this.population, {this.memeticPopulation});
  P execute(int iters) {
    P p = population.copy() as P;
    for (int i = 0; i < iters; i++) {
      p.evolve(5 % (1 + i).abs() == 0);
      reportProgress(p, i, iters);
    }
    log('LAST POPULATION: ${p.retrieveBestIndividual()}');
    return p;
  }

  P executeWithMemetic(int iters) {
    P p = population.copy() as P;
    P mp = memeticPopulation!.copy() as P;
    for (int i = 0; i < iters; i++) {
      p.evolve(5 % (1 + i).abs() == 0);
      mp.evolve(5 % (1 + i).abs() == 0);
      reportProgressWithMemetic(p, mp, i, iters);
    }
    log('LAST POPULATION: ${p.retrieveBestIndividual()}');
    return p;
  }

  void reportProgress(
      PopulationInterface<I> p, int iteration, int totalIterations) {
    log('Iteration ${iteration + 1} of $totalIterations: Best Fitness = ${p.retrieveBestIndividual().getFitness()}');
  }

  void reportProgressWithMemetic<T>(PopulationInterface<I> p,
      PopulationInterface<I> mp, int iteration, int total) {
    if (iteration % AppConfig.popSize == 0 || iteration == total - 1) {
      IndividualInterface best = p.retrieveBestIndividual();
      IndividualInterface bestMP = mp.retrieveBestIndividual();
      log('Iteration: $iteration/$total - Genetic: $best, Memetic: $bestMP');
    }
  }
}
