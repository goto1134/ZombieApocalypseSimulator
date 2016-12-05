package com.github.goto1134.zombieapocalypsesimulator.jade.ontology.data;

import jade.content.Concept;
import jade.content.onto.annotations.Slot;

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

    @Override
    public String toString() {
        return "Coordinates{" +
                "X=" + X +
                ", Y=" + Y +
                '}';
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

    public int distance(Coordinates that) {
        return Math.abs(X - that.X) + Math.abs(Y - that.Y);
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
}
