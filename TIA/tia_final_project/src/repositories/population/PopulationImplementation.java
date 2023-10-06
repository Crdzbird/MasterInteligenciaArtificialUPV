/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repositories.population;

/**
 *
 * @author crdzbird
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import repositories.IndividualInterface;
import repositories.elitism.ElitismInterface;

/**
 *
 * @author crdzbird
 * @param <I>
 */
public abstract class PopulationImplementation<I extends IndividualInterface> implements PopulationInterface<I> {

    public List<I> population;
    public final ElitismInterface<I> elitism;

    public PopulationImplementation(List<I> initpop, ElitismInterface<I> elitism) {
        this.population = new ArrayList<>(initpop);  // Ensure encapsulation
        this.elitism = elitism;
    }

    @Override
    public void evolve(boolean compact) {
        sort();
        this.population = elitism.elitism(population, compact);
        sort();
    }

    @Override
    public void sort() {
        Collections.sort(population);
    }

    @Override
    public I retrieveBestIndividual() {
        sort();
        return (I)population.get(0).copy();
    }

    public List<I> getIndividualInterface() {
        List<I> copiedPopulation = new ArrayList<>();
        population.forEach(individual -> copiedPopulation.add((I)individual.copy()));
        return copiedPopulation;
    }

    @Override
    public List<I> getMembers() {
        return new ArrayList<>(population);  // return a defensive copy
    }

    @Override
    public String toString() {
        return "PopulationImplementation{population=" + population + "}\n\n";
    }
}
