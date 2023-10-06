package com.crdzbird.models;

import java.util.Objects;

public class Path implements Comparable<Path> {
    
    private final Integer from;
    private final Integer to;

    public Path(Integer from, Integer to) {
        this.from = from;
        this.to = to;
    }

    public Integer getFrom() {
        return from;
    }

    public Integer getTo() {
        return to;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Path)) {
            return false;
        }
        Path other = (Path) obj;
        return Objects.equals(from, other.from) && Objects.equals(to, other.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }

    @Override
    public int compareTo(Path o) {
        int cmp = from.compareTo(o.from);
        return cmp != 0 ? cmp : to.compareTo(o.to);
    }
}
