package com.github.goto1134.zombieapocalypsesimulator.jade.ontology.data;

import jade.content.onto.annotations.Result;
import jade.content.onto.annotations.Slot;

/**
 * Created by Andrew
 * on 06.12.2016.
 */
@Result(type = Coordinates.class)
public class GetCoordinatesInRadius implements WalkerAction {

    private Coordinates center = new Coordinates();
    private int radius = 0;
    private Coordinates result;

    public GetCoordinatesInRadius() {
    }

    public GetCoordinatesInRadius(Coordinates center, int radius) {
        this.center = center;
        this.radius = radius;
    }

    public Coordinates getResult() {
        return result;
    }

    public void setResult(Coordinates result) {
        this.result = result;
    }

    @Slot(mandatory = true)
    public int getRadius() {
        return radius;
    }

    @Slot(mandatory = true)
    public void setRadius(int radius) {
        this.radius = radius;
    }

    @Slot(mandatory = true)
    public Coordinates getCenter() {
        return center;
    }

    @Slot(mandatory = true)
    public void setCenter(Coordinates center) {
        this.center = center;
    }
}
