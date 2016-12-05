package com.github.goto1134.zombieapocalypsesimulator.jade.walkers;

import com.github.goto1134.zombieapocalypsesimulator.ZombieApocalypseConstants;
import com.github.goto1134.zombieapocalypsesimulator.jade.controller.ApocalypseController;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Ищет контроллер, как только находит, запускает процесс регистрации
 * Created by Andrew
 * on 04.12.2016.
 */
class SearchController extends Behaviour {
    private static final Logger cat = LoggerFactory.getLogger(SearchController.class);
    private boolean found = false;

    @Override
    public void action() {
        try {
            DFAgentDescription[] search = DFService.search(getAgent(), ApocalypseController.getControllerAgentDescription());
            if (search.length > 0) {
                getDataStore().put(ZombieApocalypseConstants.CONTROLLER_AID, search[0].getName());
                found = true;
                getAgent().addBehaviour(new RequestCoordinateBehaviour(getAgent(), getDataStore()));
            }
        } catch (FIPAException e) {
            cat.error("", e);
        }
    }

    @Override
    public boolean done() {
        return found;
    }
}
