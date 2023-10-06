/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repositories;

/**
 *
 * @author crdzbird
 */

import com.crdzbird.models.Graph;
import configs.AppConfig;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class IndividualImplementation implements IndividualInterface<Integer> {
    public static double[][] distances;
    public static Graph originalGraph;

    private final Integer[] genotype;
    private float fitness = -1.0F;

    public IndividualImplementation(int startCity) {
        genotype = new Integer[distances.length];
        List<Integer> cities = getCitiesExcept(List.of(startCity));
        genotype[0] = startCity;
        IntStream.range(1, genotype.length).forEach(i -> genotype[i] = cities.remove(0));
        randomise();
    }

    private IndividualImplementation() {
        genotype = new Integer[distances.length];
    }

    public List<Integer> getGenotype() {
        return Arrays.asList(genotype);
    }

    public List<IndividualInterface> getChildren(IndividualInterface parent) {
        List<IndividualInterface> children = new ArrayList<>();
        int blockSize = AppConfig.RANDOM.nextInt(7) + 1;

        List<Integer> p1 = new ArrayList<>(Arrays.asList(genotype));
        List<Integer> p2 = new ArrayList<>(Arrays.asList(((IndividualImplementation) parent).genotype));

        IndividualImplementation child = new IndividualImplementation();

        boolean useParent1 = true;
        for (int i = 0; i < child.genotype.length; i++) {
            if (i % blockSize == 0) {
                useParent1 = !useParent1;
            }
            Integer gene = useParent1 ? p1.remove(0) : p2.remove(0);
            p1.remove(gene);
            p2.remove(gene);
            child.genotype[i] = gene;
        }

        if (AppConfig.RANDOM.nextDouble() < 0.3) {
            child.mutate();
        }

        children.add(child);
        return children;
    }

    @Override
    public List<IndividualInterface<Integer>> getNeighborhood() {
        List<IndividualInterface<Integer>> neighborhood = new ArrayList<>();

        IntStream.range(1, genotype.length).forEach(i -> IntStream.range(1, genotype.length).forEach(j -> {
            if (i != j) {
                IndividualImplementation neighbour = (IndividualImplementation) copy();
                int aux = neighbour.genotype[i];
                neighbour.genotype[i] = neighbour.genotype[j];
                neighbour.genotype[j] = aux;
                neighbour.fitness = -1.0F;
                neighborhood.add(neighbour);
            }
        }));

        return neighborhood;
    }

    public boolean isValid() {
        return true;  // If this is always true, you might consider removing this method.
    }

    public void mutate() {
        int mutations = AppConfig.RANDOM.nextInt(Math.max(1, genotype.length / 10)) + 1;
        for (int k = 0; k < mutations; k++) {
            int i, j;
            do {
                i = AppConfig.RANDOM.nextInt(genotype.length - 1) + 1;
                j = AppConfig.RANDOM.nextInt(genotype.length - 1) + 1;
            } while (i == j);

            int aux = genotype[i];
            genotype[i] = genotype[j];
            genotype[j] = aux;
        }
        fitness = -1.0F;
    }

    @Override
    public float getFitness() {
        if (fitness < 0.0F) {
            AppConfig.fitnessCalls++;
            float cost = 0.0F;
            for (int i = 0; i < genotype.length - 1; i++) {
                cost += distances[genotype[i]][genotype[i + 1]];
            }
            cost += distances[genotype[genotype.length - 1]][genotype[0]];

            int visitedEdges = originalGraph.getVisitedEdges(genotype);
            fitness = cost / (visitedEdges == AppConfig.maxNumEdges ? visitedEdges * 1000 : 1);
        }
        return fitness;
    }

    @Override
    public IndividualInterface copy() {
        IndividualImplementation ind = new IndividualImplementation();
        System.arraycopy(this.genotype, 0, ind.genotype, 0, genotype.length);
        ind.fitness = this.fitness;
        return ind;
    }

    @Override
    public void randomise() {
        List<Integer> shuffledGenes = new ArrayList<>(Arrays.asList(genotype).subList(1, genotype.length));
        Collections.shuffle(shuffledGenes);
        shuffledGenes.add(0, genotype[0]);
        IntStream.range(0, genotype.length).forEach(i -> genotype[i] = shuffledGenes.get(i));
        fitness = -1.0F;
    }

    @Override
    public int compareTo(IndividualInterface<Integer> o) {
        return Float.compare(getFitness(), o.getFitness());
    }

    public static IndividualImplementation getGreedySolution(int startCity) {
        IndividualImplementation ind = new IndividualImplementation(startCity);
        IntStream.range(1, ind.genotype.length).forEach(i -> ind.genotype[i] = getClosestCityExcept(ind.genotype[i - 1], Arrays.asList(ind.genotype).subList(0, i)));
        return ind;
    }

    private static Integer getClosestCityExcept(Integer city, List<Integer> except) {
        double minDistance = Double.MAX_VALUE;
        Integer closest = city;

        for (int i = 0; i < distances.length; i++) {
            if (!except.contains(i) && distances[city][i] < minDistance) {
                closest = i;
                minDistance = distances[city][i];
            }
        }
        return closest;
    }

    private static List<Integer> getCitiesExcept(List<Integer> except) {
        List<Integer> res = new ArrayList<>();
        IntStream.range(0, distances.length).filter(i -> !except.contains(i)).forEach(res::add);
        return res;
    }

    @Override
    public String toString() {
        return "Seed: 1633124807218\nFitness: " + getFitness() + "\nCities: " + genotype.length + "\nCppIndividual{genotype=" + Arrays.toString(genotype) + '}';
    }
}
