/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ia;

/**
 *
 * @author crdzbird
 */

import java.util.logging.Level;
import java.util.logging.Logger;
import repositories.IndividualInterface;
import repositories.population.PopulationInterface;
import ui.Graph;

public class Genetic<I extends IndividualInterface, P extends PopulationInterface<I>> {

    private static final Logger LOGGER = Logger.getLogger(Genetic.class.getName());
    private static final int REPORT_FREQUENCY = 10;

    private final PopulationInterface<I> population;
    private final Graph graph;

    public Genetic(PopulationInterface<I> population, Graph graph) {
        this.population = population;
        this.graph = graph;
    }

    /**
     * Executes the genetic algorithm for a given number of iterations.
     * 
     * @param iters the number of iterations to run the algorithm for.
     * @return the evolved population after all iterations.
     */
    public PopulationInterface<I> execute(int iters) {
        PopulationInterface<I> p = this.population.copy();

        for (int i = 0; i < iters; i++) {
            evolvePopulation(p, i);
            reportProgress(p, i, iters);
        }

        LOGGER.log(Level.INFO, "\nLAST POPULATION:\n\t{0}", p.retrieveBestIndividual());
        return p;
    }

    /**
     * Evolves the population based on given logic.
     *
     * @param p      the population to evolve.
     * @param iteration the current iteration.
     */
    private void evolvePopulation(PopulationInterface<I> p, int iteration) {
        p.evolve(5 % Math.max(1, iteration) == 0);
    }

    /**
     * Reports the progress of the algorithm and updates the graph.
     *
     * @param p        the current population.
     * @param iteration the current iteration.
     * @param total    the total number of iterations.
     */
    private void reportProgress(PopulationInterface<I> p, int iteration, int total) {
        if (iteration % REPORT_FREQUENCY == 0 || iteration == total - 1) {
            IndividualInterface best = p.retrieveBestIndividual();
            LOGGER.log(Level.INFO, "\nIteration: {0}/{1}\t{2}", new Object[]{iteration, total, best});
            graph.addData(iteration, best.getFitness());
        }
    }
}
