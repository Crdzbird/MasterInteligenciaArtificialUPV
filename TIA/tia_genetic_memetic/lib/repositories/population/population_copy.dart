import 'package:tia_genetic_memetic/repositories/elitism/elitism_abstraction.dart';
import 'package:tia_genetic_memetic/repositories/individual/individual_implementation.dart';
import 'package:tia_genetic_memetic/repositories/population/population_implementation.dart';
import 'package:tia_genetic_memetic/repositories/population/population_abstraction.dart';

class PopulationCopy
    extends PopulationImplementation<IndividualImplementation> {
  PopulationCopy(List<IndividualImplementation> initpop,
      ElitismInterface<IndividualImplementation> elitism)
      : super(initpop, elitism);

  @override
  PopulationInterface<IndividualImplementation> copy() =>
      PopulationCopy(List<IndividualImplementation>.from(population), elitism);
}
