import 'package:tia_genetic_memetic/repositories/elitism/elitism_interface.dart';
import 'package:tia_genetic_memetic/repositories/individual/individual_interface.dart';
import 'package:tia_genetic_memetic/repositories/population/population_interface.dart';

abstract class PopulationImplementation<I extends IndividualInterface>
    implements PopulationInterface<I> {
  List<I> population = List.empty(growable: true);
  final ElitismInterface<I> elitism;

  PopulationImplementation(List<I> initpop, this.elitism) {
    this.population =
        List<I>.from(initpop, growable: true); // Ensure encapsulation
  }

  @override
  void evolve(bool compact) {
    sort();
    this.population = elitism.elitism(population, compact);
    sort();
  }

  @override
  void sort() {
    population.sort();
  }

  @override
  I retrieveBestIndividual() {
    sort();
    return population[0].copy() as I;
  }

  List<I> getIndividualInterface() {
    List<I> copiedPopulation = List.empty(growable: true);
    for (var individual in population) {
      copiedPopulation.add(individual.copy() as I);
    }
    return copiedPopulation;
  }

  @override
  List<I> getMembers() {
    return List<I>.unmodifiable(population); // return an unmodifiable list view
  }

  @override
  String toString() {
    return 'PopulationImplementation{population=\$population}\\n\\n';
  }
}
