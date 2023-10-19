import 'dart:math';

class AppConfig {
  // File configurations
  static const String distanceFile =
      '/Users/crdzbird/Documents/MasterInteligenciaArtificialUPV/TIA/tia_genetic_memetic/assets/graph_raw.csv';
  static const String heuristicFile =
      '/Users/crdzbird/Documents/MasterInteligenciaArtificialUPV/TIA/tia_genetic_memetic/assets/nodesLoc.csv';

  // Genetic Algorithm and Simulated Annealing constants
  static const int maxIteration = 2000;
  static const int popSize = 10;
  static const int mainCity = 1;
  static const int memePopRate = 50;
  static const double temperature = 100.0;
  static const double coolingRate = 0.9;
  static const double mutateRate = 0.3;
  static const double replaceRate = 0.3;
  static const int compactRate = 5;

  // Random generator with a fixed seed for reproducibility
  static int seed = Random().nextInt(1633124807218);
  static final Random random = Random(seed);

  // Mutable fields
  static int fitnessCalls = 0;
  static int maxNumEdges = 0;
}

// Necessary import for the Random class
