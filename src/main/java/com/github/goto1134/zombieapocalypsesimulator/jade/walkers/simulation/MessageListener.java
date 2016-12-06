package com.github.goto1134.zombieapocalypsesimulator.jade.walkers.simulation;

import com.github.goto1134.zombieapocalypsesimulator.jade.ontology.data.BecomeZombieAction;
import com.github.goto1134.zombieapocalypsesimulator.jade.ontology.data.DieAction;
import com.github.goto1134.zombieapocalypsesimulator.jade.ontology.data.FightAction;
import com.github.goto1134.zombieapocalypsesimulator.jade.ontology.data.WalkAction;
import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Andrew
 * on 05.12.2016.
 */
class MessageListener extends Behaviour {
    private static final Logger cat = LoggerFactory.getLogger(MessageListener.class);
    private boolean received = false;
    private SimulationStates eventID = SimulationStates.LISTEN;

    @Override
    public void action() {
        cat.info("action - 26");
        ACLMessage request = getAgent().receive();
        if (request != null) {
            try {
                parseEvent(request);
            } catch (Codec.CodecException | OntologyException e) {
                ACLMessage reply = request.createReply();
                reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
                getDataStore().put(SimulationStates.RESPOND, reply);
                eventID = SimulationStates.RESPOND;
            }
        } else {
            block();
        }
    }

    private void parseEvent(ACLMessage request) throws Codec.CodecException, OntologyException {
        //Запросы что-то сделать
        if (request.getPerformative() == ACLMessage.REQUEST) {
            ContentElement contentElement = getAgent().getContentManager().extractContent(request);
            if (contentElement instanceof BecomeZombieAction) {
                cat.info("Become zombie action received");
                eventID = SimulationStates.BECOME_ZOMBIE;
                received = true;
            } else if (contentElement instanceof WalkAction) {
//                eventID = SimulationStates.WALK;
            } else if (contentElement instanceof FightAction) {
//                eventID = SimulationStates.FIGHT;
            } else if (contentElement instanceof DieAction) {
//                eventID = SimulationStates.DIE;
            }
        }

        getDataStore().put(SimulationStates.LISTEN, request);
    }

    @Override
    public boolean done() {
        return received;
    }

    @Override
    public int onEnd() {
        return eventID.ordinal();
    }

    @Override
    public void reset() {
        received = false;
        eventID = SimulationStates.LISTEN;
        super.reset();
    }
}
