package com.github.goto1134.zombieapocalypsesimulator.jade.walkers.simulation;

import com.github.goto1134.zombieapocalypsesimulator.jade.walkers.DataStoreUtils;
import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.github.goto1134.zombieapocalypsesimulator.ZombieApocalypseConstants.WALKER;
import static com.github.goto1134.zombieapocalypsesimulator.ZombieApocalypseConstants.ZOMBIE;
import static com.github.goto1134.zombieapocalypsesimulator.jade.walkers.Walker.getServiceDescription;

/**
 * Created by Andrew
 * on 05.12.2016.
 */
class BecomeZombieBehaviour extends OneShotBehaviour {
    private static final Logger cat = LoggerFactory.getLogger(BecomeZombieBehaviour.class);

    @Override
    public void action() {
        cat.info("action - 27");
        DataStore dataStore = getDataStore();

        ACLMessage message = DataStoreUtils.getReceivedMessage(dataStore);
        Agent agent = getAgent();

        String agentName = agent.getName();
        ServiceDescription humanDescription = getServiceDescription(WALKER, agentName);
        ServiceDescription zombieService = getServiceDescription(ZOMBIE, agentName);
        DFAgentDescription agentDescription = new DFAgentDescription();
        agentDescription.addServices(humanDescription);
        agentDescription.addServices(zombieService);
        agentDescription.setName(agent.getAID());
        try {
            DFService.modify(agent, agentDescription);
        } catch (FIPAException e) {
            cat.error("", e);
        }
        cat.info("Became Zombie");
        ACLMessage reply = message.createReply();
        reply.setPerformative(ACLMessage.INFORM);

        DataStoreUtils.putWalkerType(dataStore, WalkerType.ZOMBIE);
        DataStoreUtils.putRespondMessage(dataStore, reply);
    }

    @Override
    public int onEnd() {
        return SimulationStates.RESPOND.ordinal();
    }
}

