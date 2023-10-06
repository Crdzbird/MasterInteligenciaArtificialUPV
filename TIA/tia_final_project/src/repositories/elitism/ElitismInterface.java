/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package repositories.elitism;

/**
 *
 * @author crdzbird
 */

import java.util.List;
import repositories.IndividualInterface;

public interface ElitismInterface<I extends IndividualInterface> {
    List<I> elitism(List<I> individuals, boolean someFlag); // Replace 'someFlag' with a more descriptive name based on its purpose
}
