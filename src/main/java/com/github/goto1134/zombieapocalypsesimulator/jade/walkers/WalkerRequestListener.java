package com.github.goto1134.zombieapocalypsesimulator.jade.walkers;

import com.github.goto1134.zombieapocalypsesimulator.jade.ontology.MapOntology;
import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static jade.domain.FIPANames.InteractionProtocol.FIPA_REQUEST;
import static jade.lang.acl.ACLMessage.NOT_UNDERSTOOD;
import static jade.lang.acl.MessageTemplate.*;

/**
 * Created by Andrew
 * on 04.12.2016.
 */
class WalkerRequestListener extends Behaviour {

    private static final Logger cat = LoggerFactory.getLogger(WalkerRequestListener.class);
    private static final MessageTemplate template =
            and(
                    MatchOntology(MapOntology.NAME),
                    and(
                            MatchPerformative(ACLMessage.REQUEST),
                            MatchProtocol(FIPA_REQUEST)));

    private boolean received = false;

    @Override
    public void action() {
        ACLMessage request = getAgent().receive(template);
        if (request != null) {
            ContentElement contentElement;
            try {
                contentElement = getAgent().getContentManager().extractContent(request);
            } catch (Codec.CodecException | OntologyException e) {
                ACLMessage reply = request.createReply();
                reply.setPerformative(NOT_UNDERSTOOD);
                getAgent().send(reply);
                cat.error("", e);
            }

            received = true;
        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        return received;
    }
}
