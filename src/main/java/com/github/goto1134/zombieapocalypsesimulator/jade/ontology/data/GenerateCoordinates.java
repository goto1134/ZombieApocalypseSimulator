package com.github.goto1134.zombieapocalypsesimulator.jade.ontology.data;

import jade.content.AgentAction;

/**
 * Created by Andrew
 * on 28.11.2016.
 */
//@Result(type = Coordinates.class)
public class GenerateCoordinates implements AgentAction {

    private Coordinates coordinates = new Coordinates(0, 0);
    private int mapSize = 0;

    public GenerateCoordinates() {
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public int getMapSize() {
        return mapSize;
    }

    public void setMapSize(int mapSize) {
        this.mapSize = mapSize;
    }

    @Override
    public String toString() {
        return "GenerateCoordinates{" +
                "coordinates=" + coordinates +
                ", mapSize=" + mapSize +
                '}';
    }
}
