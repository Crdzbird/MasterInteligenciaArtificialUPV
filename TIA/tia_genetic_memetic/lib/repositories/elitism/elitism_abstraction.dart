import 'package:tia_genetic_memetic/repositories/individual/individual_abstraction.dart';

abstract class ElitismInterface<I extends IndividualInterface> {
  List<I> elitism(List<I> individuals, bool accepted);
}
