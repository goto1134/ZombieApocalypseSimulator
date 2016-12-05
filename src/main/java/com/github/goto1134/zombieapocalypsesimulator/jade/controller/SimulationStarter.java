package com.github.goto1134.zombieapocalypsesimulator.jade.controller;

import com.github.goto1134.zombieapocalypsesimulator.ZombieApocalypseConstants;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.proto.SimpleAchieveREInitiator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Создаёт первого зомби
 * Created by Andrew
 * on 04.12.2016.
 */
class SimulationStarter extends SimpleAchieveREInitiator {
    private static final Logger cat = LoggerFactory.getLogger(SimulationStarter.class);
    private final static ACLMessage message = new ACLMessage(ACLMessage.REQUEST);

    SimulationStarter(Agent a, DataStore store) {
        super(a, message, store);
    }

    @Override
    protected ACLMessage prepareRequest(ACLMessage msg) {
        try {
            DFAgentDescription agentDescription = new DFAgentDescription();
            ServiceDescription serviceDescription = new ServiceDescription();
            serviceDescription.setType(ZombieApocalypseConstants.HUMAN);
            agentDescription.addServices(serviceDescription);

            DFAgentDescription[] search = DFService.search(getAgent(), agentDescription);
            AID name = search[0].getName();

            ACLMessage clone = (ACLMessage) msg.clone();
            clone.addReceiver(name);

        } catch (FIPAException e) {
            cat.error("", e);
        }
        return super.prepareRequest(msg);
    }

    @Override
    protected void handleRefuse(ACLMessage msg) {
        this.reset();
    }

    @Override
    protected void handleNotUnderstood(ACLMessage msg) {
        this.reset();
    }

    @Override
    protected void handleInform(ACLMessage msg) {
        DataStore ds = new DataStore();
        ds.put(ApocalypseController.SIMULATION_PROPERTIES_KEY,
                getDataStore().get(ApocalypseController.SIMULATION_PROPERTIES_KEY));
        getAgent().addBehaviour(new SimulationChecker(getDataStore()));
    }

    @Override
    protected void handleFailure(ACLMessage msg) {
        this.reset();
    }
}
