/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ia;

import configs.AppConfig;
import java.util.Comparator;
import java.util.List;
import repositories.IndividualInterface;

/**
 *
 * @author crdzbird
 * @param <I>
 */

public class Simulated<I extends IndividualInterface> {

    private double temperature;
    private I bestIndividual;
    private I currentIndividual;
    private final double coolingRate;

    public Simulated(I initialIndividual, double initialTemperature, double coolingRate) {
        this.temperature = initialTemperature;
        this.bestIndividual = this.currentIndividual = initialIndividual;
        this.coolingRate = coolingRate;
    }

    /**
     * Runs the simulated annealing algorithm.
     * @return the best found individual.
     */
    public I start() {
        while (temperature > 1.0) {
            List<I> neighbors = currentIndividual.getNeighborhood();
            neighbors.sort(Comparator.comparingDouble(IndividualInterface::getFitness));

            I bestNeighbor = neighbors.get(0);
            double fitnessDifference = bestNeighbor.getFitness() - currentIndividual.getFitness();

            if (fitnessDifference < 0.0 || shouldAccept(fitnessDifference)) {
                currentIndividual = bestNeighbor;
                if (currentIndividual.getFitness() < bestIndividual.getFitness()) {
                    bestIndividual = currentIndividual;
                }
            }

            temperature *= coolingRate;
        }
        return bestIndividual;
    }

    /**
     * Determines if a neighbor should be accepted based on the change in fitness.
     * @param fitnessDifference difference between the fitness of the neighbor and the current individual.
     * @return true if the neighbor should be accepted, false otherwise.
     */
    private boolean shouldAccept(double fitnessDifference) {
        return AppConfig.RANDOM.nextDouble() < Math.exp(-fitnessDifference / temperature);
    }
}
