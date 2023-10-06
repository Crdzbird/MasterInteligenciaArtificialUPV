package configs;

import java.util.Random;

/**
 * Contains configuration constants for the application.
 *
 * @author crdzbird
 */
public class AppConfig {

    // File configurations
    public static final String DISTANCE_FILE = "./data/graph_raw.csv";
    public static final String HEURISTIC_FILE = "./data/nodesLoc.csv";

    // Genetic Algorithm and Simulated Annealing constants
    public static final int MAX_ITERATION = 2000;
    public static final int POP_SIZE = 10;
    public static final int MAIN_CITY = 1;
    public static final int MEME_POP_RATE = 50;
    public static final double TEMPERATURE = 100.0;
    public static final double COOLING_RATE = 0.9;
    public static final double MUTATE_RATE = 0.3;
    public static final double REPLACE_RATE = 0.3;
    public static final int COMPACT_RATE = 5;

    // Random generator with a fixed seed for reproducibility
    public static final long SEED = 1633124807218L;
    public static final Random RANDOM = new Random(SEED);

    // Mutable fields
    public static int fitnessCalls = 0;
    public static int maxNumEdges = 0;
}
