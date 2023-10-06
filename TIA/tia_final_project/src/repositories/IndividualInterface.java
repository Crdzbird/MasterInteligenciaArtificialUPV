/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package repositories;

/**
 *
 * @author crdzbird
 */

import java.util.List;

public interface IndividualInterface<E extends Comparable<E>> extends Comparable<IndividualInterface<E>> {

    List<E> getGenotype();

    List<IndividualInterface<E>> getChildren(IndividualInterface<E> partner);

    List<IndividualInterface<E>> getNeighborhood();

    boolean isValid();

    void mutate();

    float getFitness();

    IndividualInterface<E> copy();

    void randomise();

}
