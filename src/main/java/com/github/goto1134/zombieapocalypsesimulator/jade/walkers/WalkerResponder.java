package com.github.goto1134.zombieapocalypsesimulator.jade.walkers;

import com.github.goto1134.zombieapocalypsesimulator.jade.ontology.MapOntology;
import com.github.goto1134.zombieapocalypsesimulator.jade.ontology.data.BecomeZombieAction;
import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Andrew
 * on 02.12.2016.
 */
class WalkerResponder extends AchieveREResponder {
    private static final MessageTemplate TEMPLATE = MessageTemplate.MatchOntology(MapOntology.NAME);
    private static final Logger cat = LoggerFactory.getLogger(WalkerResponder.class);

    public WalkerResponder(Agent a, DataStore dataStore) {
        super(a, TEMPLATE, dataStore);
    }

    @Override
    protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
        ContentElement contentElement;
        try {
            contentElement = getAgent().getContentManager().extractContent(request);
        } catch (Codec.CodecException | OntologyException e) {
            cat.error("", e);
            throw new NotUnderstoodException(request);
        }

        if (request.getPerformative() == ACLMessage.REQUEST) {
            if (contentElement instanceof BecomeZombieAction)
            {

            }
        }
        ACLMessage reply = request.createReply();
        reply.setPerformative(ACLMessage.INFORM);
        reply.setContent("inform done");
        return reply;
    }
}
