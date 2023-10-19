import 'dart:math';

class AppConfig {
  static const String distanceFile = './assets/graph_raw.csv';
  static const String heuristicFile = './assets/nodesLoc.csv';

  static const int maxIteration = 2000;
  static const int popSize = 10;
  static const int mainCity = 1;
  static const int memePopRate = 50;
  static const double temperature = 100.0;
  static const double coolingRate = 0.9;
  static const double mutateRate = 0.3;
  static const double replaceRate = 0.3;
  static const int compactRate = 5;

  static int seed = Random().nextInt(1633124807218);
  static final Random random = Random(seed);

  static int fitnessCalls = 0;
  static int maxNumEdges = 0;
}
