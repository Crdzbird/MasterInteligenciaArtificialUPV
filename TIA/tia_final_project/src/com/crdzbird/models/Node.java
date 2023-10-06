package com.crdzbird.models;

import java.util.Objects;

public class Node<E> implements Comparable<Node<E>> {
    
    private final E value;
    private final double h;
    private final double g;
    private final Node<E> parent;

    public Node(E value, double g, double h, Node<E> parent) {
        this.value = value;
        this.h = h;
        this.g = g;
        this.parent = parent;
    }

    public double getCost() {
        return h + g;
    }

    public Node<E> getParent() {
        return parent;
    }

    public E getValue() {
        return value;
    }

    public double getH() {
        return h;
    }

    public double getG() {
        return g;
    }

    @Override
    public int compareTo(Node<E> o) {
        return Double.compare(o.getCost(), getCost()); // Simplified comparison of doubles
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Node)) {
            return false;
        }
        Node<?> other = (Node<?>) obj;
        return Objects.equals(value, other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
