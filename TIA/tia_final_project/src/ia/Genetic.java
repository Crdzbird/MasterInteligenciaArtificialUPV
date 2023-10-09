/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ia;

/**
 *
 * @author crdzbird
 */

import configs.AppConfig;
import java.util.logging.Level;
import java.util.logging.Logger;
import repositories.IndividualImplementation;
import repositories.IndividualInterface;
import repositories.population.PopulationInterface;
import ui.Graph;

public class Genetic<I extends IndividualInterface, P extends PopulationInterface<I>> {

    private static final Logger LOGGER = Logger.getLogger(Genetic.class.getName());
    private static final int REPORT_FREQUENCY = 10;

    private final PopulationInterface<I> population;
    private PopulationInterface<I> memeticPopulation;
    
    private final Graph graph;

    public Genetic(PopulationInterface<I> population, Graph graph) {
        this.population = population;
        this.graph = graph;
    }
    
    

    public Genetic(PopulationInterface<I> population, PopulationInterface<I> populationMemetic, Graph graph) {
        this.population = population;
        this.memeticPopulation = populationMemetic;
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
    
    public PopulationInterface<I> executeWithMemetic(int iters) {
        PopulationInterface<I> p = this.population.copy();
        PopulationInterface<I> mp = this.memeticPopulation.copy();
        
        for (int i = 0; i < iters; i++) {
            evolveBothPopulation(p, mp, i);
            reportProgressWithMemetic(p, mp, i, iters);
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
     * Evolves the population based on given logic.
     *
     * @param p      the population to evolve.
     * @param iteration the current iteration.
     */
    private void evolveBothPopulation(PopulationInterface<I> p, PopulationInterface<I> mp, int iteration) {
        p.evolve(5 % Math.max(1, iteration) == 0);
        mp.evolve(5 % Math.max(1, iteration) == 0);
        if (iteration % AppConfig.MEME_POP_RATE == 0) {
                applyMemeticOperations(p, AppConfig.TEMPERATURE, AppConfig.COOLING_RATE);
            }
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
            graph.addData(iteration, best.getFitness(), 0);
        }
    }
    
    
    private void applyMemeticOperations(PopulationInterface<I> p, double temperature, double coolingRate) {
        IndividualInterface best = p.retrieveBestIndividual();
        IndividualInterface other = p.getMembers().get(AppConfig.RANDOM.nextInt(p.getMembers().size()));

        IndividualInterface bestAfterSA = (I) (new Simulated<>(best, temperature, coolingRate)).start();
        IndividualInterface otherAfterSA = (I) (new Simulated<>(other, temperature, coolingRate)).start();

        p.getMembers().remove(best);
        p.getMembers().remove(other);

        p.getMembers().add(0, (I) bestAfterSA);
        p.getMembers().add((I) otherAfterSA);
    }
    
    
    /**
     * Reports the progress of the algorithm and updates the graph.
     *
     * @param p        the current population.
     * @param iteration the current iteration.
     * @param total    the total number of iterations.
     */
    private void reportProgressWithMemetic(PopulationInterface<I> p, PopulationInterface<I> mp, int iteration, int total) {
        if (iteration % REPORT_FREQUENCY == 0 || iteration == total - 1) {
            IndividualInterface best = p.retrieveBestIndividual();
            IndividualInterface bestMP = mp.retrieveBestIndividual();
            LOGGER.log(Level.INFO, "\nIteration: {0}/{1}\tGenetic:{2}\tMemetic:{3}", new Object[]{iteration, total, best, bestMP});
            graph.addData(iteration, best.getFitness(), bestMP.getFitness());
        }
    }
}
