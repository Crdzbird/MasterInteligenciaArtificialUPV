import 'dart:collection';

import 'package:tia_genetic_memetic/models/city.dart';
import 'package:tia_genetic_memetic/models/node.dart';
import 'package:tia_genetic_memetic/models/path.dart';
import 'package:tia_genetic_memetic/service/city_dao.dart';

class Graph {
  late List<List<double>>
      distances; // 'late' indicates the variable is non-nullable but will be initialized later
  final Map<Path, List<int>> paths =
      SplayTreeMap(); // TreeMap in Java, closest in Dart is SplayTreeMap

  Graph(int size) {
    distances =
        List.generate(size, (i) => List.filled(size, -1.0, growable: false));
  }

  void setDistance(int i, int j, double distance) {
    distances[i][j] = distance;
  }

  double getDistance(int i, int j) {
    return distances[i][j];
  }

  void calculateAllPaths() {
    int size = distances.length;
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        if (i != j) {
          paths[Path(i, j)] = getPath(i, j);
        }
      }
    }
  }

  int getVisitedEdges(List<int> trail) {
    int visitedEdges = 0;
    List<List<bool>> visited = List.generate(
        distances.length, (_) => List.filled(distances.length, false));

    for (int i = 0; i < trail.length - 1; i++) {
      int city1 = trail[i];
      int city2 = trail[i + 1];
      List<int> path = paths[Path(city1, city2)]!;
      for (int k = 0; k < path.length - 1; k++) {
        if (!visited[path[k]][path[k + 1]]) {
          visited[path[k]][path[k + 1]] = true;
          visited[path[k + 1]][path[k]] = true;
          visitedEdges++;
        }
      }
    }

    // Assuming the trail is circular, connecting the last and the first cities
    int c1 = trail[trail.length - 1];
    int c2 = trail[0];
    List<int> lastPath = paths[Path(c1, c2)]!;
    for (int k = 0; k < lastPath.length - 1; k++) {
      if (!visited[lastPath[k]][lastPath[k + 1]]) {
        visited[lastPath[k]][lastPath[k + 1]] = true;
        visited[lastPath[k + 1]][lastPath[k]] =
            true; // Corrected here from `path` to `lastPath`
        visitedEdges++;
      }
    }

    return visitedEdges;
  }

  Graph getFullConnectedGraph(double bias) {
    Graph g = Graph(distances.length);

    for (int i = 0; i < distances.length; i++) {
      for (int j = 0; j < distances[i].length; j++) {
        g.setDistance(i, j, getDistance(i, j));
      }
    }

    g.fullConnect(bias);

    return g;
  }

  void fullConnect(double bias) {
    for (int i = 0; i < distances.length; i++) {
      for (int j = 0; j < distances[i].length; j++) {
        if (i != j && getDistance(i, j) < 0.0) {
          double distance = evaluatePath(getPath(i, j)) * bias;
          setDistance(i, j, distance);
          setDistance(j, i, distance);
        }
      }
    }
  }

  double evaluatePath(List<int> path) {
    double cost = 0.0;

    for (int i = 0; i < path.length - 1; i++) {
      cost += getDistance(path[i], path[i + 1]);
    }

    return cost;
  }

  List<int> getPath(int start, int end) {
    List<int> path = [];
    List<Node> openList = [];
    List<Node> closeList = [];

    openList.add(Node(
        start,
        0.0,
        City.manhattanDistance(CityDao.getAt(start)!, CityDao.getAt(end)!),
        null));

    while (openList.isNotEmpty) {
      openList.sort((a, b) => a.compareTo(b));
      Node? currentNode = openList.removeAt(0);

      if (currentNode.value == end) {
        Node? solution = currentNode;
        while (solution != null) {
          path.insert(0, solution.value); // adds to the beginning of the list
          solution = solution.parent;
        }
        openList.clear();
        continue;
      }

      closeList.add(currentNode);

      for (int connection in getConnections(currentNode.value)) {
        Node c = Node(
            connection,
            getDistance(currentNode.value, connection) + currentNode.g,
            City.manhattanDistance(
                CityDao.getAt(currentNode.value)!, CityDao.getAt(connection)!),
            currentNode);

        bool inOpenList = openList.contains(c);
        bool inCloseList = closeList.contains(c);

        if (inOpenList) {
          int indexInOpenList = openList.indexOf(c);
          if (c.cost < openList[indexInOpenList].g) {
            openList.removeAt(indexInOpenList);
          }
        } else if (inCloseList) {
          int indexInCloseList = closeList.indexOf(c);
          if (c.cost < closeList[indexInCloseList].g) {
            closeList.removeAt(indexInCloseList);
          }
        }

        if (!inOpenList && !inCloseList) {
          openList.add(c);
        }
      }
    }
    return path;
  }

  List<int> getConnections(int vertex) {
    List<int> conn = [];

    for (int i = 0; i < distances.length; i++) {
      if (i != vertex && distances[vertex][i] >= 0.0) {
        conn.add(i);
      }
    }

    return conn;
  }

  List<List<double>> getDistanceMatrix() {
    List<List<double>> dis = List.generate(distances.length,
        (i) => List.from(distances[i])); // creates a copy of each row

    return dis;
  }

  @override
  String toString() {
    return 'Graph{distances: \$distances, paths: \$paths}';
  }
}
