/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package repositories.population;

/**
 *
 * @author crdzbird
 */

import java.util.List;
import repositories.IndividualInterface;

public interface PopulationInterface<I extends IndividualInterface> {

    void evolve(boolean shouldEvolve);

    void sort();

    I retrieveBestIndividual();

    PopulationInterface<I> copy();

    List<I> getMembers();

}
