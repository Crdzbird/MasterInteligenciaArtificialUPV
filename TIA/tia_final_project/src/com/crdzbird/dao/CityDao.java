/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.crdzbird.dao;

/**
 *
 * @author crdzbird
 */

import com.crdzbird.models.City;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CityDao {
    
    private static final List<City> CITIES = new ArrayList<>();

    public static City create(City c) {
        Objects.requireNonNull(c, "City object cannot be null");
        if (!CITIES.contains(c)) {
            CITIES.add(c);
        }
        return c;
    }
    
    public static City remove(City c) {
        CITIES.remove(c);
        return c;
    }

    public static City get(String name) {
        for (City city : CITIES) {
            if (city.getName().equals(name)) {
                return city;
            }
        }
        return null;
    }
    
    public static City get(int index) {
        return CITIES.get(index);
    }
    
    public static int getIndex(City c) {
        return CITIES.indexOf(c);
    }

    public static List<City> getAll() {
        return new ArrayList<>(CITIES);  // Return a copy for safety
    }
}
