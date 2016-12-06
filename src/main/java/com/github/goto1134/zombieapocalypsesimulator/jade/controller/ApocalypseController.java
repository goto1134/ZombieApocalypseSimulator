package com.github.goto1134.zombieapocalypsesimulator.jade.controller;

import com.github.goto1134.zombieapocalypsesimulator.jade.ontology.ApocalypseOntology;
import com.github.goto1134.zombieapocalypsesimulator.view.SimulationProperties;
import jade.content.lang.leap.LEAPCodec;
import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Andrew
 * on 27.11.2016.
 */
public class ApocalypseController extends Agent {
    public static final String SIMULATION_PROPERTIES_KEY = "SIMULATION_PROPERTIES_KEY";
    private static final Logger cat = LoggerFactory.getLogger(ApocalypseController.class);

    @Override
    protected void setup() {
        super.setup();

        try {
            registerService();
        } catch (FIPAException e) {
            cat.error("", e);
        }

        Object[] arguments = getArguments();
        if (arguments.length != 1) {
            doDelete();
        } else {
            SimulationProperties properties = (SimulationProperties) arguments[0];
            DataStore store = new DataStore();
            store.put(SIMULATION_PROPERTIES_KEY, properties);

            addBehaviour(new RegistrarBehaviour(this, store));
            getContentManager().registerOntology(ApocalypseOntology.getInstance());
            getContentManager().registerLanguage(new LEAPCodec());
            cat.info("Loaded");
        }
    }

    private void registerService() throws FIPAException {
        DFAgentDescription dfAgentDescription = getControllerAgentDescription();

        dfAgentDescription.setName(getAID());
        DFService.register(this, dfAgentDescription);
    }

    public static DFAgentDescription getControllerAgentDescription() {
        DFAgentDescription dfAgentDescription = new DFAgentDescription();
        ServiceDescription service = new ServiceDescription();
        service.setName("controller");
        service.setType("controller");
        service.addLanguages(new LEAPCodec().getName());
        service.addOntologies(ApocalypseOntology.NAME);
        dfAgentDescription.addServices(service);
        return dfAgentDescription;
    }
}
