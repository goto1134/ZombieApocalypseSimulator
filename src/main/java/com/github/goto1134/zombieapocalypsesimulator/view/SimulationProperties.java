package com.github.goto1134.zombieapocalypsesimulator.view;

/**
 * Created by Andrew
 * on 04.12.2016.
 */
public class SimulationProperties {
    public final int walkersNumber;
    public final int weaponNumber;
    public final int mapSize;

    public SimulationProperties(int characterNumber, int weaponNumber, int mapSize) {
        this.walkersNumber = characterNumber;
        this.weaponNumber = weaponNumber;
        this.mapSize = mapSize;
    }
}
