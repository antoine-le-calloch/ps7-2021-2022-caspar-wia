package me.polyfrontier.casparwia2.model;

import javax.persistence.*;

@Embeddable
public class Freight {

    private String type;

    private String name;

    private double weight;

    public Freight() {
    }

    public Freight(String type, String name, double weight) {
        this.type = type;
        this.name = name;
        this.weight = weight;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public double getWeight() {
        return weight;
    }
}
