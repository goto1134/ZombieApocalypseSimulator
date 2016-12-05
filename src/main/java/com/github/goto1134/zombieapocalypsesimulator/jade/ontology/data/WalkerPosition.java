package com.github.goto1134.zombieapocalypsesimulator.jade.ontology.data;

import jade.content.Concept;
import jade.content.onto.annotations.Slot;
import jade.core.AID;

/**
 * Created by Andrew
 * on 27.11.2016.
 */
public class WalkerPosition implements Concept {

    private AID name;
    private Coordinates coordinates;

    public WalkerPosition(AID name, Coordinates coordinates) {
        this.name = name;
        this.coordinates = coordinates;
    }

    @Slot(mandatory = true)
    public AID getName() {
        return name;
    }

    public void setName(AID name) {
        this.name = name;
    }

    @Slot(mandatory = true)
    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }
}
