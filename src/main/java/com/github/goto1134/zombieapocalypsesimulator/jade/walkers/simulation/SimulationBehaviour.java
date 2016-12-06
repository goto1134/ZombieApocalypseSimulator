package com.github.goto1134.zombieapocalypsesimulator.jade.walkers.simulation;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.FSMBehaviour;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import static com.github.goto1134.zombieapocalypsesimulator.jade.walkers.simulation.SimulationStates.*;

/**
 * Created by Andrew
 * on 05.12.2016.
 */
public class SimulationBehaviour extends FSMBehaviour {

    private static final Logger cat = LoggerFactory.getLogger(SimulationBehaviour.class);

    public SimulationBehaviour(Agent a) {
        super(a);
        DataStore dataStore = getDataStore();

        //Обработка входящих сообщений
        Behaviour messageListener = new MessageListener();
        messageListener.setDataStore(dataStore);
        registerFirstState(messageListener, LISTEN.toString());

        //Стать зомби
        Behaviour becomeZombieBehaviour = new BecomeZombieBehaviour();
        becomeZombieBehaviour.setDataStore(dataStore);
        registerState(becomeZombieBehaviour, SimulationStates.BECOME_ZOMBIE);

        //Ответ
        Behaviour respondBehaviour = new RespondBehaviour();
        respondBehaviour.setDataStore(dataStore);
        registerState(respondBehaviour, SimulationStates.RESPOND);

        //
        Behaviour walkBehaviour = new WalkBehaviour();
        walkBehaviour.setDataStore(dataStore);
        registerState(walkBehaviour, SimulationStates.WALK);


        registerTransition(LISTEN, SimulationStates.BECOME_ZOMBIE);
        registerTransition(LISTEN, SimulationStates.RESPOND);
//        registerTransition(LISTEN, GET_ALL_IN_RADIUS);
//        registerTransition(LISTEN, SimulationStates.PREPARE_COORDINATES);
//        registerTransition(GET_ALL_IN_RADIUS, WALK);
//        registerTransition(GET_ALL_IN_RADIUS, FIGHT);
//        registerTransition(GET_ALL_IN_RADIUS, DIE);
        registerDefaultTransition(SimulationStates.BECOME_ZOMBIE, SimulationStates.RESPOND);
        registerDefaultTransition(SimulationStates.WALK, SimulationStates.RESPOND);
//        registerDefaultTransition(SimulationStates.FIGHT, SimulationStates.RESPOND);
//        registerTransition(SimulationStates.DIE, SimulationStates.RESPOND);
//        registerTransition(SimulationStates.DIE, SimulationStates.BECOME_ZOMBIE);
//        registerDefaultTransition(SimulationStates.PREPARE_COORDINATES, SimulationStates.RESPOND);
        registerTransition(SimulationStates.RESPOND, LISTEN, LISTEN/* SimulationStates.values()*/);
    }

    private void registerState(Behaviour b, SimulationStates state) {
        registerState(b, state.toString());
    }

    private void registerTransition(SimulationStates s1, SimulationStates s2) {
        registerTransition(s1.toString(), s2.toString(), s2.ordinal());
    }

    private void registerDefaultTransition(SimulationStates s1, SimulationStates s2) {
        registerDefaultTransition(s1.toString(), s2.toString());
    }

    private void registerTransition(SimulationStates s1, SimulationStates s2, SimulationStates... toBeReset) {
        String[] convertedToBeReset = Arrays.stream(toBeReset)
                .map(SimulationStates::toString)
                .toArray(String[]::new);
        registerTransition(s1.toString(), s2.toString(), s2.ordinal(), convertedToBeReset);
    }

    @Override
    public void reset() {
        DataStore dataStore = getDataStore();
        dataStore.remove(LISTEN);
        dataStore.remove(SimulationStates.RESPOND);
        super.reset();
    }

}