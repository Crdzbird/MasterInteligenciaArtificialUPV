import 'dart:math';
import 'dart:developer' as logger;
import 'package:tia_genetic_memetic/repositories/individual/individual_abstraction.dart';

class Simulated<I extends IndividualInterface> {
  double temperature;
  I bestIndividual;
  I currentIndividual;
  final double coolingRate;

  Simulated(this.currentIndividual, double initialTemperature, this.coolingRate)
      : temperature = initialTemperature,
        bestIndividual = currentIndividual;

  I start() {
    while (temperature > 1.0) {
      var neighbors = currentIndividual.getNeighborhood().cast<I>();
      neighbors.sort((a, b) => a.getFitness().compareTo(b.getFitness()));

      I bestNeighbor = neighbors[0];
      double fitnessDifference =
          bestNeighbor.getFitness() - currentIndividual.getFitness();

      if (fitnessDifference < 0.0 || _shouldAccept(fitnessDifference)) {
        currentIndividual = bestNeighbor;
        if (currentIndividual.getFitness() < bestIndividual.getFitness()) {
          bestIndividual = currentIndividual;
        }
      }
      temperature *= coolingRate;
      logger.log('CURRENT TEMPERATURE: $temperature');
    }
    logger.log('temperature: $temperature');
    return bestIndividual;
  }

  bool _shouldAccept(double fitnessDifference) {
    if (fitnessDifference < 0) return true;
    return Random().nextDouble() < exp(-fitnessDifference / temperature);
  }
}
