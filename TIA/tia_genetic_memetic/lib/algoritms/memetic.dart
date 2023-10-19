import 'package:tia_genetic_memetic/algoritms/simulated.dart';
import 'package:tia_genetic_memetic/config/app_config.dart';
import 'package:tia_genetic_memetic/repositories/individual/individual_interface.dart';
import 'package:tia_genetic_memetic/repositories/population/population_interface.dart';

class Memetic<I extends IndividualInterface, P extends PopulationInterface<I>> {
  final P population;

  Memetic(this.population);

  P execute(
      int iters, int memeticRate, double temperature, double coolingRate) {
    P p = population.copy() as P;

    for (int i = 0; i < iters; i++) {
      p.evolve(5 % (1 + i).abs() == 0);

      if (i % memeticRate == 0) {
        _applyMemeticOperations(p, temperature, coolingRate);
      }

      _reportProgress(p, i, iters);
    }

    print('\\nLAST POPULATION:\\n\\t${p.retrieveBestIndividual()}');
    return p;
  }

  void _applyMemeticOperations(
      PopulationInterface<I> p, double temperature, double coolingRate) {
    var best = p.retrieveBestIndividual();
    var other = p.getMembers()[AppConfig.random
        .nextInt(p.getMembers().length)]; // Assuming you have AppConfig in Dart

    var bestAfterSA = Simulated(best, temperature, coolingRate)
        .start(); // Assuming the start function's implementation
    var otherAfterSA = Simulated(other, temperature, coolingRate).start();

    p.getMembers().remove(best);
    p.getMembers().remove(other);

    p.getMembers().insert(0, bestAfterSA);
    p.getMembers().insert(0, otherAfterSA);
  }

  void _reportProgress(
      PopulationInterface<I> p, int iteration, int totalIterations) {
    // This method can be used to report progress of the algorithm.
    // Implementation can vary depending on how you want to report this progress.
    // For simplicity, we'll print the current iteration and the best individual's fitness.
    print(
        'Iteration ${iteration + 1} of $totalIterations: Best Fitness = ${p.retrieveBestIndividual().getFitness()}');
  }
}