package com.github.goto1134.zombieapocalypsesimulator.jade.walkers.simulation;

import com.github.goto1134.zombieapocalypsesimulator.jade.ontology.data.DieAction;
import com.github.goto1134.zombieapocalypsesimulator.jade.ontology.data.WalkerPosition;
import com.github.goto1134.zombieapocalypsesimulator.jade.walkers.DataStoreUtils;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.SimpleAchieveREInitiator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Vector;

import static com.github.goto1134.zombieapocalypsesimulator.jade.MessageUtils.prepareMessage;

/**
 * Created by Andrew
 * on 06.12.2016.
 */
class FightBehaviour extends SimpleAchieveREInitiator {

    private static final Logger cat = LoggerFactory.getLogger(FightBehaviour.class);
    private static final ACLMessage message = prepareMessage(ACLMessage.REQUEST);
    private DataStore dataStore;

    public FightBehaviour(Agent a, DataStore dataStore) {
        super(a, message);
        this.dataStore = dataStore;
    }

    @Override
    protected ACLMessage prepareRequest(ACLMessage msg) {
        List<WalkerPosition> coordinateList = DataStoreUtils.getCoordinateList(dataStore);
        if (coordinateList.isEmpty()) {
            setRespondMessage();
            return null;
        } else {
            ACLMessage request = (ACLMessage) msg.clone();
            request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
            request.addReceiver(coordinateList.get(0).getName());
            try {
                getAgent().getContentManager().fillContent(request, new DieAction());
            } catch (Codec.CodecException | OntologyException e) {
                cat.error("", e);
            }
            return request;
        }
    }

    @Override
    protected void handleAllResultNotifications(Vector msgs) {
        setRespondMessage();
    }

    @Override
    public void reset() {
        super.reset(message);
    }

    private void setRespondMessage() {
        ACLMessage receivedMessage = DataStoreUtils.getReceivedMessage(dataStore);
        ACLMessage reply = receivedMessage.createReply();
        reply.setPerformative(ACLMessage.INFORM);
        DataStoreUtils.putRespondMessage(dataStore, reply);
    }
}
