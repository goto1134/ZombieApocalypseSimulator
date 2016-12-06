package com.github.goto1134.zombieapocalypsesimulator.jade.walkers;

import com.github.goto1134.zombieapocalypsesimulator.ZombieApocalypseConstants;
import com.github.goto1134.zombieapocalypsesimulator.jade.ontology.ApocalypseOntology;
import com.github.goto1134.zombieapocalypsesimulator.jade.ontology.data.GenerateCoordinates;
import com.github.goto1134.zombieapocalypsesimulator.jade.walkers.simulation.SimulationBehaviour;
import com.github.goto1134.zombieapocalypsesimulator.jade.walkers.simulation.WalkerType;
import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.lang.acl.ACLMessage;
import jade.proto.SimpleAchieveREInitiator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static jade.domain.FIPANames.InteractionProtocol.FIPA_QUERY;

/**
 * Получает данные из контроллера
 * Created by Andrew
 * on 04.12.2016.
 */
class RequestCoordinateBehaviour extends SimpleAchieveREInitiator {
    private static final Logger cat = LoggerFactory.getLogger(RequestCoordinateBehaviour.class);
    private final static ACLMessage message = new ACLMessage(ACLMessage.QUERY_REF);

    static {
        message.setOntology(ApocalypseOntology.NAME);
        message.setProtocol(FIPA_QUERY);
    }

    RequestCoordinateBehaviour(Agent a, DataStore store) {
        super(a, message, store);
    }

    @Override
    protected ACLMessage prepareRequest(ACLMessage msg) {
        ACLMessage clone = (ACLMessage) msg.clone();
        AID controllerAID = (AID) getDataStore().get(ZombieApocalypseConstants.CONTROLLER_AID);
        clone.setLanguage(getAgent().getContentManager().getLanguageNames()[0]);
        clone.addReceiver(controllerAID);

        try {
            getAgent().getContentManager().fillContent(clone, new GenerateCoordinates());
        } catch (Codec.CodecException | OntologyException e) {
            cat.error("", e);
        }

        cat.info("Sent");
        return clone;
    }

    @Override
    protected void handleInform(ACLMessage msg) {

        try {
            ContentElement contentElement = getAgent().getContentManager().extractContent(msg);
            GenerateCoordinates generateCoordinates = (GenerateCoordinates) contentElement;
            cat.info("Inform " + generateCoordinates);

            DataStore dataStore = new DataStore();
            DataStoreUtils.putGeneratedCoordinates(dataStore, generateCoordinates);
            DataStoreUtils.putWalkerType(dataStore, WalkerType.HUMAN);
            DataStoreUtils.putHumanState(dataStore, HumanState.CALM);

            SimulationBehaviour simulationBehaviour = new SimulationBehaviour(getAgent(), dataStore);
            RespondCoordinateBehaviour respondCoordinateBehaviour = new RespondCoordinateBehaviour(getAgent(), dataStore);
            getAgent().addBehaviour(respondCoordinateBehaviour);
            getAgent().addBehaviour(simulationBehaviour);
        } catch (Codec.CodecException | OntologyException e) {
            cat.error("", e);
        }
    }
}
