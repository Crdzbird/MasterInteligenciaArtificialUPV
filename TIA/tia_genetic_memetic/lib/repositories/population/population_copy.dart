import 'package:tia_genetic_memetic/repositories/elitism/elitism_interface.dart';
import 'package:tia_genetic_memetic/repositories/individual/individual_implementation.dart';
import 'package:tia_genetic_memetic/repositories/population/population_implementation.dart';
import 'package:tia_genetic_memetic/repositories/population/population_interface.dart';

class PopulationCopy
    extends PopulationImplementation<IndividualImplementation> {
  PopulationCopy(List<IndividualImplementation> initpop,
      ElitismInterface<IndividualImplementation> elitism)
      : super(initpop, elitism);

  @override
  PopulationInterface<IndividualImplementation> copy() {
    return PopulationCopy(
        List<IndividualImplementation>.from(population), elitism);
  }
}
