
package com.crdzbird.models;

/**
 *
 * @author crdzbird
 */

import java.util.Objects;

public class City implements Comparable<City> {
    
    private final String name;
    private final float x;
    private final float y;

    public City(String name, float x, float y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public static double euclideanDistance(City c1, City c2) {
        double dx = c2.getX() - c1.getX();
        double dy = c2.getY() - c1.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    public static double manhattanDistance(City c1, City c2) {
        return Math.abs(c1.getX() - c2.getX()) + Math.abs(c1.getY() - c2.getY());
    }

    @Override
    public int compareTo(City o) {
        return name.compareTo(o.getName());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof City)) {
            return false;
        }
        City other = (City) obj;
        return Objects.equals(name, other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "City {name=".concat(name).concat(", x=").concat(String.valueOf(x)).concat(", y=").concat(String.valueOf(y)).concat("}\n");
    }
}
