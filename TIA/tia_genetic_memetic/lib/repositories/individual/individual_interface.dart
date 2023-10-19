import 'dart:core';

abstract class IndividualInterface<E>
    implements Comparable<IndividualInterface<E>> {
  List<E> getGenotype();

  List<IndividualInterface<E>> getChildren(IndividualInterface<E> partner);

  List<IndividualInterface<E>> getNeighborhood();

  bool isValid();

  void mutate();

  double
      getFitness(); // In Dart, there's no distinction between float and double, so we use double

  IndividualInterface<E> copy();

  void randomise();
}
