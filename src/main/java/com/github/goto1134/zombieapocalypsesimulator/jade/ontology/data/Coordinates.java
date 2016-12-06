package com.github.goto1134.zombieapocalypsesimulator.jade.ontology.data;

import jade.content.Concept;
import jade.content.onto.annotations.Slot;

import java.util.Random;

/**
 * Created by Andrew
 * on 27.11.2016.
 */
public class Coordinates implements Concept {

    private int X;
    private int Y;


    public Coordinates() {

    }

    public Coordinates(int x, int y) {
        X = x;
        Y = y;
    }

    public Coordinates randomInRadius(int radius) {
        Random random = new Random(System.currentTimeMillis());
        Coordinates newCoordinates = null;
        do {
            int bound = 2 * radius;
            newCoordinates = new Coordinates(X + random.nextInt(bound) - radius, Y + random.nextInt(bound) - radius);
        }
        while (newCoordinates.distance(this) > radius || newCoordinates.getX() < 0 || newCoordinates.getY() < 0);
        return newCoordinates;
    }

    public int distance(Coordinates that) {
        return Math.abs(X - that.X) + Math.abs(Y - that.Y);
    }

    @Slot(mandatory = true)
    public int getX() {
        return X;
    }

    public void setX(int x) {
        X = x;
    }

    @Slot(mandatory = true)
    public int getY() {
        return Y;
    }

    public void setY(int y) {
        Y = y;
    }

    @Override
    public int hashCode() {
        int result = X;
        result = 31 * result + Y;
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Coordinates that = (Coordinates) o;

        return X == that.X && Y == that.Y;
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "X=" + X +
                ", Y=" + Y +
                '}';
    }
}
