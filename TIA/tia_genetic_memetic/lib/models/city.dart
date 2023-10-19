import 'dart:math' as math;

class City implements Comparable<City> {
  final String name;
  final double x;
  final double y;

  City(this.name, this.x, this.y);

  String get getName => name;

  double get getX => x;

  double get getY => y;

  static double euclideanDistance(City c1, City c2) {
    double dx = c2.x - c1.x;
    double dy = c2.y - c1.y;
    return math.sqrt(dx * dx + dy * dy);
  }

  static double manhattanDistance(City c1, City c2) {
    return (c1.x - c2.x).abs() + (c1.y - c2.y).abs();
  }

  @override
  int compareTo(City other) {
    return name.compareTo(other.name);
  }

  @override
  bool operator ==(Object other) {
    if (identical(this, other)) return true;

    if (other is City) {
      return name == other.name;
    }

    return false;
  }

  @override
  int get hashCode => name.hashCode;

  @override
  String toString() {
    return 'City {name: $name, x: $x, y: $y}';
  }
}
