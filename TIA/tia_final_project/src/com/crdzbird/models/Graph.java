/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.crdzbird.models;

import com.crdzbird.dao.CityDao;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author crdzbird
 */


public class Graph {
    private final double[][] distances;
    private final Map<Path, List<Integer>> paths = new TreeMap<>();

    public Graph(int size) {
        this.distances = new double[size][size];
        for (double[] row : distances) {
            Arrays.fill(row, -1.0D);
        }
    }

    public void setDistance(int i, int j, double distance) {
        this.distances[i][j] = distance;
    }

    public double getDistance(int i, int j) {
        return this.distances[i][j];
    }

    public void calculateAllPaths() {
        int size = distances.length;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i != j) {
                    this.paths.put(new Path(i, j), getPath(i, j));
                }
            }
        }
    }
    
    public int getVisitedEdges(Integer[] trail) {
     int visitedEdges = 0;
     boolean[][] visited = new boolean[this.distances.length][this.distances.length];
     
     for (int i = 0; i < trail.length - 1; i++) {
       Integer integer1 = trail[i];
       Integer integer2 = trail[i + 1];
       List<Integer> list = this.paths.get(new Path(integer1, integer2));
       for (int k = 0; k < list.size() - 1; k++) {
         if (!visited[(list.get(k))][(list.get(k + 1))]) {
           visited[(list.get(k))][(list.get(k + 1))] = true;
           visited[(list.get(k + 1))][(list.get(k))] = true;
           visitedEdges++;
         } 
       } 
     } 
 
     
     Integer c1 = trail[trail.length - 1];
     Integer c2 = trail[0];
     List<Integer> path = this.paths.get(new Path(c1, c2));
     
     for (int j = 0; j < path.size() - 1; j++) {
       if (!visited[(path.get(j))][(path.get(j + 1))]) {
         visited[(path.get(j))][(path.get(j + 1))] = true;
         visited[(path.get(j + 1))][(path.get(j))] = true;
         visitedEdges++;
       } 
     } 
     
     return visitedEdges;
   }
   
   public Graph getFullConectedGraph(double bias) throws InterruptedException {
     Graph g = new Graph(this.distances.length);
     
     for (int i = 0; i < this.distances.length; i++) {
       for (int j = 0; j < this.distances.length; j++) {
         g.setDistance(i, j, getDistance(i, j));
       }
     } 
     
     g.fullConnect(bias);
     
     return g;
   }
    
    private void fullConnect(double bias) throws InterruptedException {
     for (int i = 0; i < this.distances.length; i++) {
       
       for (int j = 0; j < this.distances.length; j++) {
         
         if (i != j && getDistance(i, j) < 0.0D) {
           
           double distance = evaluatePath(getPath(i, j)) * bias;
           setDistance(i, j, distance);
           setDistance(j, i, distance);
         } 
       } 
     } 
   }

   private double evaluatePath(List<Integer> path) {
     double cost = 0.0D;
     
     for (int i = 0; i < path.size() - 1; i++) {
       cost += getDistance((path.get(i)), (path.get(i + 1)));
     }
     
     return cost;
   }

    public List<Integer> getPath(Integer start, Integer end) {
        List<Integer> path = new LinkedList<>();
        List<Node<Integer>> openList = new LinkedList<>();
        List<Node<Integer>> closeList = new LinkedList<>();

        openList.add(new Node<>(start, 0.0D, City.manhattanDistance(CityDao.get(start), CityDao.get(end)), null));

        while (!openList.isEmpty()) {
            Collections.sort(openList);
            Node<Integer> currentNode = openList.remove(0);

            if (currentNode.getValue().equals(end)) {
                Node<Integer> solution = currentNode;
                while (solution != null) {
                    path.add(0, solution.getValue());
                    solution = solution.getParent();
                }
                openList.clear();
                continue;
            }

            closeList.add(currentNode);

            for (Integer connection : getConnections(currentNode.getValue())) {
                Node<Integer> c = new Node<>(connection, getDistance(currentNode.getValue(), connection) + currentNode.getG(), City.manhattanDistance(CityDao.get(currentNode.getValue()), CityDao.get(connection)), currentNode);

                // The next block replaces repetitive contains and get combination logic
                boolean inOpenList = openList.contains(c);
                boolean inCloseList = closeList.contains(c);

                if (inOpenList && c.getCost() < openList.get(openList.indexOf(c)).getG()) {
                    openList.remove(c);
                } else if (inCloseList && c.getCost() < closeList.get(closeList.indexOf(c)).getG()) {
                    closeList.remove(c);
                }

                if (!inOpenList && !inCloseList) {
                    openList.add(c);
                }
            }
        }
        return path;
    }

    private List<Integer> getConnections(int vertex) {
     List<Integer> conn = new ArrayList<>();
     
     for (int i = 0; i < this.distances.length; i++) {
       if (i != vertex && this.distances[vertex][i] >= 0.0D) {
         conn.add(i);
       }
     } 
 
     
     return conn;
   }
    
    public double[][] getDistanceMatrix() {
     double[][] dis = new double[this.distances.length][this.distances.length];
     
     for (int i = 0; i < this.distances.length; i++) {
       System.arraycopy(this.distances[i], 0, dis[i], 0, this.distances.length);
     }
     
     return dis;
   }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (double[] row : distances) {
            for (double distance : row) {
                s.append(" ").append(distance);
            }
            s.append("\n");
        }
        return s.toString();
    }
}
