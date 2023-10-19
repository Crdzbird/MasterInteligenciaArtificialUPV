import 'package:tia_genetic_memetic/repositories/individual/individual_interface.dart';

abstract class ElitismInterface<I extends IndividualInterface> {
  List<I> elitism(
      List<I> individuals,
      bool
          someFlag); // Replace 'someFlag' with a more descriptive name based on its purpose
}
