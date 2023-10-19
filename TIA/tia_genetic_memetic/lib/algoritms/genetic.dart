import 'dart:math';

import 'package:tia_genetic_memetic/repositories/individual/individual_interface.dart';
import 'package:tia_genetic_memetic/repositories/population/population_interface.dart';

class Genetic<I extends IndividualInterface, P extends PopulationInterface<I>> {
  final P population;
  P? memeticPopulation; // This is nullable, as it seems optional in the Java code

  Genetic(this.population, {this.memeticPopulation}); // Constructor

  P execute(int iters) {
    P p = population.copy()
        as P; // Assuming a copy method exists and works as in Java

    for (int i = 0; i < iters; i++) {
      p.evolve(5 % (1 + i).abs() ==
          0); // Placeholder for evolution logic, replace with actual method call
      reportProgress(p, i, iters); // Assuming you want to keep similar logging
    }

    print(
        'LAST POPULATION: ${p.retrieveBestIndividual()}'); // Simplified logging, consider a better option
    return p;
  }

  P executeWithMemetic(int iters) {
    P p = population.copy() as P;
    P mp = memeticPopulation!.copy()
        as P; // Using the non-null assertion operator, as memeticPopulation is nullable

    for (int i = 0; i < iters; i++) {
      p.evolve(5 % (1 + i).abs() ==
          0); // Placeholder for evolution logic, replace with actual method call
      mp.evolve(5 % (1 + i).abs() == 0); // Same for the memetic population
      reportProgressWithMemetic(
          p, mp, i, iters); // Assuming you want to keep similar logging
    }

    print(
        'LAST POPULATION: ${p.retrieveBestIndividual()}'); // Simplified logging, consider a better option
    return p;
  }

  void reportProgress(
      PopulationInterface<I> p, int iteration, int totalIterations) {
    // This method can be used to report progress of the algorithm.
    // Implementation can vary depending on how you want to report this progress.
    // For simplicity, we'll print the current iteration and the best individual's fitness.
    print(
        'Iteration ${iteration + 1} of $totalIterations: Best Fitness = ${p.retrieveBestIndividual().getFitness()}');
  }

  void reportProgressWithMemetic<T>(PopulationInterface<I> p,
      PopulationInterface<I> mp, int iteration, int total) {
    // Assuming Graph is a class you've defined that has an addData method
    if (iteration % 10 == 0 || iteration == total - 1) {
      IndividualInterface best = p.retrieveBestIndividual();
      IndividualInterface bestMP = mp.retrieveBestIndividual();
      print('Iteration: $iteration/$total\tGenetic: $best\tMemetic: $bestMP');
    }
  }
}
