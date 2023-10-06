/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repositories.population;

/**
 *
 * @author crdzbird
 */

import java.util.List;
import repositories.IndividualImplementation;
import repositories.elitism.ElitismInterface;

public class PopulationCopy extends PopulationImplementation<IndividualImplementation> {

    public PopulationCopy(List<IndividualImplementation> initpop, ElitismInterface<IndividualImplementation> elitism) {
        super(initpop, elitism);
    }

    @Override
    public PopulationInterface<IndividualImplementation> copy() {
        return new PopulationCopy(this.population, this.elitism);
    }
}
