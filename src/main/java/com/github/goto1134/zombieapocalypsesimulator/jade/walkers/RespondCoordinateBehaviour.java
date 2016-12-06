package com.github.goto1134.zombieapocalypsesimulator.jade.walkers;

import com.github.goto1134.zombieapocalypsesimulator.jade.ontology.data.Coordinates;
import com.github.goto1134.zombieapocalypsesimulator.jade.ontology.data.GetCoordinatesInRadius;
import jade.content.ContentManager;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.SimpleAchieveREResponder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Andrew
 * on 06.12.2016.
 */
class RespondCoordinateBehaviour extends SimpleAchieveREResponder {
    private static final Logger cat = LoggerFactory.getLogger(RespondCoordinateBehaviour.class);
    private static MessageTemplate template = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.QUERY_REF),
            MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_QUERY));
    private final DataStore dataStore;

    RespondCoordinateBehaviour(Agent a, DataStore dataStore) {
        super(a, template);
        this.dataStore = dataStore;
    }

    @Override
    protected ACLMessage prepareResponse(ACLMessage request)
            throws NotUnderstoodException, RefuseException {
        GetCoordinatesInRadius getCoordinatesInRadius;
        Agent agent = getAgent();
        ContentManager contentManager = agent.getContentManager();

        try {
            getCoordinatesInRadius = (GetCoordinatesInRadius) contentManager.extractContent(request);
        } catch (Codec.CodecException | OntologyException e) {
            cat.info("not understood");
            throw new NotUnderstoodException(request);
        }

        Coordinates coordinates = DataStoreUtils.getCoordinates(dataStore);

        if (coordinates.distance(getCoordinatesInRadius.getCenter()) > getCoordinatesInRadius.getRadius()) {
            cat.info("refused");
            throw new RefuseException(request);
        }
        getCoordinatesInRadius.setResult(coordinates);

        ACLMessage reply = request.createReply();
        reply.setPerformative(ACLMessage.INFORM);
        try {
            contentManager.fillContent(reply, getCoordinatesInRadius);
        } catch (Codec.CodecException | OntologyException e) {
            cat.info("refused");
            throw new RefuseException(request);
        }
        cat.info(agent.getName() + " Inform  coordinates ");
        return reply;
    }

    @Override
    protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response)
            throws FailureException {
        return response;
    }
}
