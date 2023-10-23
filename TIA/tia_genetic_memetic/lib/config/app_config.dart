import 'dart:math';

class AppConfig {
  static const String distanceFile = './assets/graph_raw.csv';
  static const String heuristicFile = './assets/nodesLoc.csv';

  static const int maxIteration = 2000; // maximum number of iterations
  static const int populationSize = 10; // population size
  static const int mainCity = 1; // starting city id
  static const int memePopulationRate = 50; // population rate
  static const double temperature = 100.0; // starting temperature
  static const double coolingRate = 0.9; // cooling rate
  static const double mutateRate = 0.3; // mutation rate
  static const double replaceRate = 0.3; // replacement rate
  static const int compactRate = 5; // compaction rate

  static int seed = 1633124807218;
  static final Random random = Random(seed);

  static int fitnessCalls = 0;
  static int maxNumEdges = 0;
}
