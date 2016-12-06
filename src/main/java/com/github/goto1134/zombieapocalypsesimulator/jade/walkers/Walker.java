package com.github.goto1134.zombieapocalypsesimulator.jade.walkers;

import com.github.goto1134.zombieapocalypsesimulator.jade.ontology.ApocalypseOntology;
import jade.content.lang.leap.LEAPCodec;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.github.goto1134.zombieapocalypsesimulator.ZombieApocalypseConstants.HUMAN;
import static com.github.goto1134.zombieapocalypsesimulator.ZombieApocalypseConstants.WALKER;

/**
 * Created by Andrew
 * on 25.11.2016.
 */
public class Walker extends Agent {

    private static final Logger cat = LoggerFactory.getLogger(Walker.class);

    @Override
    protected void setup() {
        cat.info("Setup");
        getContentManager().registerOntology(ApocalypseOntology.getInstance());
        getContentManager().registerLanguage(new LEAPCodec());
        try {
            String agentName = getName();
            ServiceDescription walkerDescription = getServiceDescription(WALKER, agentName);
            ServiceDescription humanDescription = getServiceDescription(HUMAN, agentName);
            DFAgentDescription agentDescription = new DFAgentDescription();
            agentDescription.addServices(walkerDescription);
            agentDescription.addServices(humanDescription);
            agentDescription.setName(getAID());
            DFService.register(this, agentDescription);
        } catch (FIPAException e) {
            cat.error("", e);
        }

        addBehaviour(new SearchController());
        cat.info("Laded");
    }

    public static ServiceDescription getServiceDescription(String type, String agentName) {
        ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setType(type);
        serviceDescription.setName(agentName + type);
        return serviceDescription;
    }

    @Override
    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (FIPAException e) {
            cat.error("", e);
        }
        super.takeDown();
    }
}
