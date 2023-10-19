import 'package:tia_genetic_memetic/repositories/individual/individual_interface.dart';

abstract class PopulationInterface<I extends IndividualInterface> {
  void evolve(bool shouldEvolve);
  void sort();
  I retrieveBestIndividual();
  PopulationInterface<I> copy();
  List<I> getMembers();
}
