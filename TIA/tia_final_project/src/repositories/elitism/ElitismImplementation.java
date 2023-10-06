/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repositories.elitism;

import configs.AppConfig;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import repositories.IndividualImplementation;
import repositories.IndividualInterface;

/**
 *
 * @author crdzbird
 */

public class ElitismImplementation implements ElitismInterface<IndividualImplementation> {

    public List<IndividualImplementation> elitism(List<IndividualImplementation> population, boolean compact) {
        List<IndividualImplementation> nextGeneration = new ArrayList<>(population.size());

        Collections.sort(population);

        int eliteCount = Math.round(population.size() * 0.2F);
        nextGeneration.addAll(population.subList(0, eliteCount));

        int startingCity = population.get(0).getGenotype().get(0);

        addNewCppIndividuals(nextGeneration, startingCity, eliteCount);

        while (nextGeneration.size() < population.size()) {
            IndividualImplementation parent1 = selectRandomIndividual(population);
            IndividualImplementation parent2 = selectRandomIndividual(population);
            nextGeneration.add((IndividualImplementation) parent1.getChildren((IndividualInterface) parent2).get(0));
        }

        if (compact) {
            Set<IndividualImplementation> compactedPop = new HashSet<>(nextGeneration);
            nextGeneration.clear();
            nextGeneration.addAll(compactedPop);
            addNewCppIndividuals(nextGeneration, startingCity, population.size() - nextGeneration.size());
        }

        Collections.sort(nextGeneration);

        return nextGeneration;
    }

    private void addNewCppIndividuals(List<IndividualImplementation> list, int startingCity, int count) {
        for (int i = 0; i < count; i++) {
            list.add(new IndividualImplementation(startingCity));
        }
    }

    private IndividualImplementation selectRandomIndividual(List<IndividualImplementation> population) {
        return population.get(AppConfig.RANDOM.nextInt(population.size()));
    }
}
