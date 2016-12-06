package com.github.goto1134.zombieapocalypsesimulator.jade.walkers.simulation;

import com.github.goto1134.zombieapocalypsesimulator.jade.MessageUtils;
import com.github.goto1134.zombieapocalypsesimulator.jade.ontology.data.BecomeZombieAction;
import com.github.goto1134.zombieapocalypsesimulator.jade.ontology.data.FightAction;
import com.github.goto1134.zombieapocalypsesimulator.jade.ontology.data.WalkAction;
import com.github.goto1134.zombieapocalypsesimulator.jade.walkers.DataStoreUtils;
import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.DataStore;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.github.goto1134.zombieapocalypsesimulator.jade.walkers.simulation.SimulationStates.FIGHT;
import static com.github.goto1134.zombieapocalypsesimulator.jade.walkers.simulation.SimulationStates.WALK;
import static jade.lang.acl.MessageTemplate.*;

/**
 * Created by Andrew
 * on 05.12.2016.
 */
class MessageListener extends Behaviour {
    private static final Logger cat = LoggerFactory.getLogger(MessageListener.class);
    private boolean received = false;
    private SimulationStates eventID = SimulationStates.LISTEN;
    private MessageTemplate template =
            and(
                    MatchPerformative(ACLMessage.REQUEST),
                    and(
                            not(
                                    MatchContent(MessageUtils.DieActionContent)),
                            not(
                                    MatchContent(MessageUtils.ShoutActionContent))));

    @Override
    public void action() {
        ACLMessage request = getAgent().receive(template);
        if (request != null) {
            received = true;
            try {
                parseEvent(request);
            } catch (Codec.CodecException | OntologyException e) {
                putNotUnderstoodRespond(request);
            }
        } else {
            block();
        }
    }

    private void parseEvent(ACLMessage request) throws Codec.CodecException, OntologyException {
        //Запросы что-то сделать
        DataStore dataStore = getDataStore();
        if (request.getPerformative() == ACLMessage.REQUEST) {
            ContentElement contentElement = getAgent().getContentManager().extractContent(request);
            if (contentElement instanceof BecomeZombieAction) {
                cat.info("Become zombie action received");
                eventID = SimulationStates.BECOME_ZOMBIE;
            } else if (contentElement instanceof WalkAction) {
                cat.info("Walk received");
                DataStoreUtils.putGetAllInRadiusType(dataStore, WALK);
                eventID = SimulationStates.GET_ALL_IN_RADIUS;
            } else if (contentElement instanceof FightAction) {
                cat.info("Fight received");
                DataStoreUtils.putGetAllInRadiusType(dataStore, FIGHT);
                eventID = SimulationStates.GET_ALL_IN_RADIUS;
            } else {
                putNotUnderstoodRespond(request);
            }
        }
        DataStoreUtils.putReceivedMessage(dataStore, request);
    }

    private void putNotUnderstoodRespond(ACLMessage request) {
        ACLMessage reply = request.createReply();
        reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
        DataStoreUtils.putRespondMessage(getDataStore(), reply);
        eventID = SimulationStates.RESPOND;
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
        DataStoreUtils.putRespondMessage(getDataStore(), null);
        super.reset();
    }
}
