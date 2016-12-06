package com.github.goto1134.zombieapocalypsesimulator.jade.walkers;

import com.github.goto1134.zombieapocalypsesimulator.jade.ontology.MapOntology;
import jade.content.lang.leap.LEAPCodec;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Andrew
 * on 25.11.2016.
 */
public class Walker extends Agent {

    private static final Logger cat = LoggerFactory.getLogger(Walker.class);

    @Override
    protected void setup() {
        cat.info("Setup");
        getContentManager().registerOntology(MapOntology.getInstance());
        getContentManager().registerLanguage(new LEAPCodec());

//
//        try {
//            ServiceDescription description = new ServiceDescription();
//            description.setType(ZOMBIE.equals(type) ? ZOMBIE : HUMAN);
//            description.setName(String.valueOf(new Random(System.currentTimeMillis()).nextInt()));
//            DFAgentDescription agentDescription = new DFAgentDescription();
//            agentDescription.addServices(description);
//            agentDescription.setName(getAID());
//
//            DFService.register(this, agentDescription);
//        } catch (FIPAException e) {
//            cat.error("", e);
//        }

        addBehaviour(new SearchController());
        cat.info("Laded");
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
