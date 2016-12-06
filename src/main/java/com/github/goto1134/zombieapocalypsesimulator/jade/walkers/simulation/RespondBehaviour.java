package com.github.goto1134.zombieapocalypsesimulator.jade.walkers.simulation;

import com.github.goto1134.zombieapocalypsesimulator.jade.walkers.DataStoreUtils;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Andrew
 * on 05.12.2016.
 */
class RespondBehaviour extends OneShotBehaviour {
    private static final Logger cat = LoggerFactory.getLogger(RespondBehaviour.class);

    @Override
    public void action() {
        cat.info("action - 13");
        ACLMessage message = DataStoreUtils.getRespondMessage(getDataStore());
        if (message == null) {
            throw new NullPointerException();
        }
        getAgent().send(message);
    }

    @Override
    public int onEnd() {
        return SimulationStates.LISTEN.ordinal();
    }
}
