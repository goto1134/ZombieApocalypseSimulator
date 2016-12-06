package com.github.goto1134.zombieapocalypsesimulator.jade.walkers.simulation;

import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

/**
 * Created by Andrew
 * on 05.12.2016.
 */
class RespondBehaviour extends OneShotBehaviour {
    @Override
    public void action() {
        ACLMessage message = (ACLMessage) getDataStore().get(SimulationStates.RESPOND);
        getAgent().send(message);
    }

    @Override
    public int onEnd() {
        return SimulationStates.LISTEN.ordinal();
    }
}
