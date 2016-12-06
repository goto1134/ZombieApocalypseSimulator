package com.github.goto1134.zombieapocalypsesimulator.jade.walkers;

import com.github.goto1134.zombieapocalypsesimulator.ZombieApocalypseConstants;
import com.github.goto1134.zombieapocalypsesimulator.jade.ontology.data.Coordinates;
import com.github.goto1134.zombieapocalypsesimulator.jade.ontology.data.GenerateCoordinates;
import com.github.goto1134.zombieapocalypsesimulator.jade.walkers.simulation.SimulationStates;
import com.github.goto1134.zombieapocalypsesimulator.jade.walkers.simulation.WalkerType;
import jade.core.behaviours.DataStore;
import jade.lang.acl.ACLMessage;

import java.util.List;

import static com.github.goto1134.zombieapocalypsesimulator.jade.walkers.simulation.SimulationStates.*;

/**
 * Created by Andrew
 * on 06.12.2016.
 */
public class DataStoreUtils {
    public static Coordinates getCoordinates(DataStore dataStore) {
        return ((Coordinates) dataStore.get(ZombieApocalypseConstants.COORDINATE));
    }

    public static void putGeneratedCoordinates(DataStore dataStore, GenerateCoordinates generateCoordinates) {
        putCoordinates(dataStore, generateCoordinates.getCoordinates());
        putMapSize(dataStore, generateCoordinates.getMapSize());
    }

    public static void putCoordinates(DataStore dataStore, Coordinates coordinates) {
        dataStore.put(ZombieApocalypseConstants.COORDINATE, coordinates);
    }

    private static void putMapSize(DataStore dataStore, int mapSize) {
        dataStore.put(ZombieApocalypseConstants.MAP_SIZE, mapSize);
    }

    public static int getMapSize(DataStore dataStore) {
        return ((int) dataStore.get(ZombieApocalypseConstants.MAP_SIZE));
    }

    public static void putHumanState(DataStore dataStore, HumanState humanState) {
        dataStore.put(ZombieApocalypseConstants.HUMAN_STATE, humanState);
    }

    public static HumanState getHumanState(DataStore dataStore) {
        return (HumanState) dataStore.get(ZombieApocalypseConstants.HUMAN_STATE);
    }

    public static void putWalkerType(DataStore dataStore, WalkerType walkerType) {
        dataStore.put(ZombieApocalypseConstants.WALKER_TYPE, walkerType);
    }

    public static WalkerType getWalkerType(DataStore dataStore) {
        return (WalkerType) dataStore.get(ZombieApocalypseConstants.WALKER_TYPE);
    }

    public static void putReceivedMessage(DataStore dataStore, ACLMessage message) {
        putMessage(dataStore, message, LISTEN);
    }

    private static void putMessage(DataStore dataStore, ACLMessage message, SimulationStates status) {
        dataStore.put(status, message);
    }

    public static void putRespondMessage(DataStore dataStore, ACLMessage message) {
        putMessage(dataStore, message, RESPOND);
    }


    public static ACLMessage getReceivedMessage(DataStore dataStore) {
        return getMessage(dataStore, LISTEN);
    }

    private static ACLMessage getMessage(DataStore dataStore, SimulationStates status) {
        return (ACLMessage) dataStore.get(status);
    }

    public static ACLMessage getRespondMessage(DataStore dataStore) {
        return getMessage(dataStore, RESPOND);
    }

    public static void putGetAllInRadiusType(DataStore dataStore, SimulationStates state) {
        dataStore.put(GET_ALL_IN_RADIUS, state);
    }

    public static SimulationStates getGetAllInRadiusType(DataStore dataStore) {
        return (SimulationStates) dataStore.get(GET_ALL_IN_RADIUS);
    }

    public static void putCoordinateList(DataStore dataStore, List<Coordinates> coordinates) {
        dataStore.put(GET_ALL_IN_RADIUS, coordinates);
    }

    public static List<Coordinates> getCoordinateList(DataStore dataStore) {
        return (List<Coordinates>) dataStore.get(GET_ALL_IN_RADIUS);
    }

}
