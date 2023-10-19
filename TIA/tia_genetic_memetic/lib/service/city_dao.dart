import 'package:tia_genetic_memetic/models/city.dart';

class CityDao {
  static final List<City> _cities = List.empty(growable: true);

  static City create(City? c) {
    if (c == null) {
      throw ArgumentError('City object cannot be null');
    }
    if (!_cities.contains(c)) {
      _cities.add(c);
    }
    return c;
  }

  static City remove(City c) {
    _cities.remove(c);
    return c;
  }

  static City? get(String name) {
    for (City city in _cities) {
      if (city.name == name) {
        return city;
      }
    }
    return null;
  }

  static City? getAt(int index) {
    if (index < 0 || index >= _cities.length) {
      return null; // or throw an exception
    }
    return _cities[index];
  }

  static int getIndex(City c) {
    return _cities.indexOf(c);
  }

  static List<City> getAll() {
    return List<City>.unmodifiable(_cities);
  }
}
