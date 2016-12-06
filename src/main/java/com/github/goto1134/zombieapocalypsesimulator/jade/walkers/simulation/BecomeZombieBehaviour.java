package com.github.goto1134.zombieapocalypsesimulator.jade.walkers.simulation;

import com.github.goto1134.zombieapocalypsesimulator.ZombieApocalypseConstants;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Andrew
 * on 05.12.2016.
 */
class BecomeZombieBehaviour extends OneShotBehaviour {
    private static final Logger cat = LoggerFactory.getLogger(BecomeZombieBehaviour.class);

    @Override
    public void action() {
        ACLMessage message = (ACLMessage) getDataStore().get(SimulationStates.LISTEN);
        getDataStore().put(ZombieApocalypseConstants.WALKER_TYPE, ZombieApocalypseConstants.ZOMBIE);
        try {
            DFService.deregister(getAgent());
        } catch (FIPAException e) {
            cat.error("", e);
        }
        // TODO: 05.12.2016 Зарегистрировать сервис зомби

        ACLMessage reply = message.createReply();
        reply.setPerformative(ACLMessage.INFORM);
        getDataStore().put(SimulationStates.RESPOND, reply);
    }

    @Override
    public int onEnd() {
        return SimulationStates.RESPOND.ordinal();
    }
}

