import 'dart:math';

class AppConfig {
  static const String distanceFile = './assets/graph_raw.csv';
  static const String heuristicFile = './assets/nodesLoc.csv';

  static const int maxIteration =
      2000; // maximum number of iterations for the algorithm
  static const int populationSize =
      10; // population size for the genetic/memetic algorithm
  static const int mainCity =
      1; // possibly the starting city or main node in the problem
  static const int memePopulationRate =
      50; // likely related to the population rate in the memetic algorithm
  static const double temperature =
      100.0; // starting temperature for simulated annealing
  static const double coolingRate = 0.9; // cooling rate for simulated annealing
  static const double mutateRate =
      0.3; // mutation rate for the genetic/memetic algorithm
  static const double replaceRate =
      0.3; // replacement rate for the genetic/memetic algorithm
  static const int compactRate =
      5; // purpose unclear, possibly related to a rate of compaction or reduction in some aspect of the algorithm

  static int seed = Random().nextInt(1633124807218);
  static final Random random = Random(seed);

  static int fitnessCalls = 0;
  static int maxNumEdges = 0;
}
