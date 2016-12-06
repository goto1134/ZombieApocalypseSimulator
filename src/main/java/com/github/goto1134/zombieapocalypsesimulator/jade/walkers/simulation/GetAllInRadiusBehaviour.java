package com.github.goto1134.zombieapocalypsesimulator.jade.walkers.simulation;

import com.github.goto1134.zombieapocalypsesimulator.ZombieApocalypseConstants;
import com.github.goto1134.zombieapocalypsesimulator.jade.DFUtils;
import com.github.goto1134.zombieapocalypsesimulator.jade.ontology.data.Coordinates;
import com.github.goto1134.zombieapocalypsesimulator.jade.ontology.data.GetCoordinatesInRadius;
import com.github.goto1134.zombieapocalypsesimulator.jade.walkers.DataStoreUtils;
import com.github.goto1134.zombieapocalypsesimulator.jade.walkers.WalkerType;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

import static com.github.goto1134.zombieapocalypsesimulator.jade.MessageUtils.prepareMessage;

/**
 * Created by Andrew
 * on 06.12.2016.
 */
class GetAllInRadiusBehaviour extends AchieveREInitiator {
    private static final Logger cat = LoggerFactory.getLogger(GetAllInRadiusBehaviour.class);
    private static ACLMessage message = prepareMessage(ACLMessage.QUERY_REF);
    private final DataStore dataStore;
    private SimulationStates nextState;

    public GetAllInRadiusBehaviour(Agent a, DataStore dataStore) {
        super(a, message);
        this.dataStore = dataStore;
    }


    @Override
    protected Vector prepareRequests(ACLMessage request) {
        nextState = DataStoreUtils.getGetAllInRadiusType(dataStore);
        WalkerType walkerType = DataStoreUtils.getWalkerType(dataStore);

        DFAgentDescription description = walkerType.equals(WalkerType.HUMAN)
                ? ZombieApocalypseConstants.ZOMBIE_AGENT_DESCRIPTION
                : ZombieApocalypseConstants.HUMAN_AGENT_DESCRIPTION;
        try {
            Vector<ACLMessage> aclMessages = DFUtils.getAllAgentsByType(getAgent(), description)
                    .map(this::getRequest)
                    .collect(Collectors.toCollection(Vector::new));
            cat.info(String.valueOf(aclMessages));
            return aclMessages;
        } catch (FIPAException e) {
            cat.error("", e);
        }
        return super.prepareRequests(request);
    }

    @Override
    protected void handleAllResponses(Vector responses) {
        List<Coordinates> coordinates = ((Vector<ACLMessage>) responses).stream()
                .filter(aclMessage -> aclMessage.getPerformative() == ACLMessage.INFORM)
                .map(this::getCoordinates)
                .collect(Collectors.toList());
        DataStoreUtils.putCoordinateList(dataStore, coordinates);
    }

    private Coordinates getCoordinates(ACLMessage message) {
        try {
            GetCoordinatesInRadius getCoordinatesInRadius = (GetCoordinatesInRadius) getAgent().getContentManager().extractContent(message);
            return getCoordinatesInRadius.getResult();
        } catch (Codec.CodecException | OntologyException e) {
            cat.error("", e);
            return null;
        }
    }

    private ACLMessage getRequest(AID receiver) {
        ACLMessage clone = ((ACLMessage) message.clone());
        clone.addReceiver(receiver);
        clone.setProtocol(FIPANames.InteractionProtocol.FIPA_QUERY);

        // TODO: 06.12.2016 Проверить, в случае, если это атака, сменить радиус на 0
        int radius = 2;

        Coordinates center = DataStoreUtils.getCoordinates(dataStore);
        try {
            getAgent().getContentManager().fillContent(clone, new GetCoordinatesInRadius(center, radius));
        } catch (Codec.CodecException | OntologyException e) {
            cat.error("", e);
        }
        return clone;
    }

    @Override
    public int onEnd() {
        return nextState.ordinal();
    }
}
