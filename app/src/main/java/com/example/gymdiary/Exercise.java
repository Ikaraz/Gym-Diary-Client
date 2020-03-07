package com.example.gymdiary;

public class Exercise {
    private int id;
    private String name;
    private int reps;
    private double weight;

    public Exercise(Integer id, String name, int reps, Double weight) {
        this.id = id;
        this.name = name;
        this.reps = reps;
        this.weight = weight;
    }
    public Integer getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public Integer getReps() {
        return reps;
    }
    public Double getWeight() {
        return weight;
    }
}
