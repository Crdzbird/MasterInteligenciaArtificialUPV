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
import repositories.IndividualInterface;
import repositories.population.PopulationInterface;
import ui.Graph;

public class Memetic<I extends IndividualInterface, P extends PopulationInterface<I>> {
    private static final Logger LOGGER = Logger.getLogger(Memetic.class.getName());

    private final PopulationInterface<I> population;
    private final Graph graph;

    public Memetic(PopulationInterface<I> population, Graph graph) {
        this.population = population;
        this.graph = graph;
    }

    public PopulationInterface<I> execute(int iters, int memeticRate, double temperature, double coolingRate) {
        PopulationInterface<I> p = this.population.copy();

        for (int i = 0; i < iters; i++) {
            p.evolve(5 % Math.max(1, i) == 0);

            if (i % memeticRate == 0) {
                applyMemeticOperations(p, temperature, coolingRate);
            }

            reportProgress(p, i, iters);
        }

        LOGGER.log(Level.INFO, "\nLAST POPULATION:\n\t{0}", p.retrieveBestIndividual());
        return p;
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

    private void reportProgress(PopulationInterface<I> p, int iteration, int total) {
        if (iteration % 100 == 0 && iteration > 0) {
            LOGGER.info(p.toString());
        }
        IndividualInterface best = p.retrieveBestIndividual();
        LOGGER.log(Level.INFO, "\nIteration: {0}/{1}\t{2}", new Object[]{iteration, total, best});
        graph.addData(iteration, 0, best.getFitness());
    }
}
